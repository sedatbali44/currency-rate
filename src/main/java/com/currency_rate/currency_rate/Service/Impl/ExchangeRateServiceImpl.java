package com.currency_rate.currency_rate.Service.Impl;

import com.currency_rate.currency_rate.Client.ExchangeRateProviderService;
import com.currency_rate.currency_rate.Dto.*;
import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import com.currency_rate.currency_rate.Entity.ExchangeRate;
import com.currency_rate.currency_rate.Exception.ExceptionHandlerService;
import com.currency_rate.currency_rate.Repo.ExchangeRateRepository;
import com.currency_rate.currency_rate.Service.ConversionHistoryService;
import com.currency_rate.currency_rate.Service.ExchangeRateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository repo;

    @Autowired
    private ExchangeRateProviderService exchangeRateProviderService;

    @Autowired
    private ExceptionHandlerService exceptionHandlerService;

    @Autowired
    private ConversionHistoryService conversionHistoryService;

    @Override
    @Transactional
    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        log.debug("Getting exchange rate for {} to {}", request.getSourceCurrency(), request.getTargetCurrency());
        Currency sourceCurrency = request.getSourceCurrency();
        Currency targetCurrency = request.getTargetCurrency();

        exceptionHandlerService.validateCurrencyCode(sourceCurrency, sourceCurrency.name());
        exceptionHandlerService.validateCurrencyCode(targetCurrency, targetCurrency.name());

        Double rate = exchangeRateProviderService.convert(sourceCurrency, targetCurrency);

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .rate(BigDecimal.valueOf(rate))
                .timestamp(LocalDateTime.now())
                .build();

        repo.save(exchangeRate);

        ExchangeRateResponse exchangeRateResponse = ExchangeRateResponse.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .rate(exchangeRate.getRate())
                .timestamp(exchangeRate.getTimestamp())
                .build();

        return exchangeRateResponse;
    }

    @Override
    @Transactional
    public ConversionResponse calculateRateAmount(ConversionRequest request) {
        Double amount = request.getAmount();
        Double rate = exchangeRateProviderService.convert(request.getSourceCurrency(), request.getTargetCurrency());
        BigDecimal convertedAmount = calculateConvertedAmount(request.getAmount(), rate);

        String message = String.format("%.2f %s is equal to %.2f %s",
                amount,
                request.getSourceCurrency(),
                convertedAmount,
                request.getTargetCurrency());

        ConversionHistory conversionHistory = conversionHistoryService.createConversionHistory(
                request, BigDecimal.valueOf(rate), BigDecimal.valueOf(amount), convertedAmount);

        if (conversionHistory == null) {
            log.info("Conversion could not save for: {} to {}", request.getSourceCurrency(), request.getTargetCurrency());
        }

        return ConversionResponse.builder()
                .amount(convertedAmount)
                .sourceCurrency(request.getSourceCurrency())
                .targetCurrency(request.getTargetCurrency())
                .rate(BigDecimal.valueOf(rate))
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
    }


    private BigDecimal calculateConvertedAmount(Double amount, Double rate) {
        Double convertedAmount = amount * rate;
        return BigDecimal.valueOf(convertedAmount);
    }

    @Override
    @Transactional
    public ResponseEntity<String> calculateConversions(MultipartFile file) {
        Currency defaultCurrency = Currency.USD;
        Map<String, Double> allRates = exchangeRateProviderService.getAllRates(defaultCurrency);
        int rowSize = 0;
        try {
            List<ConversionRequest> conversionRequests = parseCSV(file);
            for (ConversionRequest conversionRequest : conversionRequests) {
                Double sourceRate = allRates.get(conversionRequest.getSourceCurrency().name());
                Double targetRate = allRates.get(conversionRequest.getTargetCurrency().name());

                BigDecimal amount = BigDecimal.valueOf(conversionRequest.getAmount());
                BigDecimal usdAmount = amount.divide(BigDecimal.valueOf(sourceRate), 10, RoundingMode.HALF_UP);
                BigDecimal convertedAmount = usdAmount.multiply(BigDecimal.valueOf(targetRate));

                ConversionHistory conversionHistory = conversionHistoryService.createConversionHistory(
                        conversionRequest,
                        BigDecimal.valueOf(sourceRate),
                        amount,
                        convertedAmount
                );

                if (conversionHistory != null) {
                    rowSize++;
                }
            }

            return ResponseEntity.ok("CSV file processed successfully. " + rowSize + " records saved.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process CSV file: " + e.getMessage());
        }
    }

    @Override
    public List<ConversionRequest> parseCSV(MultipartFile file) throws IOException {
        List<ConversionRequest> conversionRequests = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(";");
                if (values.length >= 3) {
                    String sourceCurrencyStr = values[0].trim();
                    String targetCurrencyStr = values[1].trim();
                    String amountStr = values[2].trim();

                    try {
                        Currency sourceCurrency = Currency.valueOf(sourceCurrencyStr.toUpperCase());
                        Currency targetCurrency = Currency.valueOf(targetCurrencyStr.toUpperCase());
                        double amount = Double.parseDouble(amountStr);

                        ConversionRequest request = ConversionRequest.builder()
                                .sourceCurrency(sourceCurrency)
                                .targetCurrency(targetCurrency)
                                .amount(amount)
                                .build();

                        conversionRequests.add(request);

                        // Log successful parsing
                        System.out.println("Successfully parsed: " + sourceCurrencyStr + " to " +
                                targetCurrencyStr + " amount: " + amountStr);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line: " + line + " - Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid CSV format in line: " + line +
                            " - Expected at least 3 fields separated by semicolons");
                }
            }
        }

        System.out.println("Total conversion requests parsed: " + conversionRequests.size());
        return conversionRequests;
    }

}
