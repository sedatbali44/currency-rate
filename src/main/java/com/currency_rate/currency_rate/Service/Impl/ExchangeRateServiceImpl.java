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
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        BigDecimal convertedAmount = calculateConvertedAmount(request.getAmount(),rate);

        String message = String.format("%.2f %s is equal to %.2f %s",
                amount,
                request.getSourceCurrency(),
                convertedAmount,
                request.getTargetCurrency());

        ConversionHistory conversionHistory = conversionHistoryService.createConversionHistory(
                request, BigDecimal.valueOf(rate), BigDecimal.valueOf(amount),convertedAmount);

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
    public void calculateConversions(ConversionsRequest request) {
        Currency defaultCurrency = Currency.USD;
        Map<String, Double> allRates = exchangeRateProviderService.getAllRates(defaultCurrency);
        //conversionHistoryService.saveAll(null);
    }
}
