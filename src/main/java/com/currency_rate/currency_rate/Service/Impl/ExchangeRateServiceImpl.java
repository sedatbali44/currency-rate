package com.currency_rate.currency_rate.Service.Impl;

import com.currency_rate.currency_rate.Client.ExchangeRateProviderService;
import com.currency_rate.currency_rate.Dto.ConversionRequest;
import com.currency_rate.currency_rate.Dto.ConversionResponse;
import com.currency_rate.currency_rate.Dto.ExchangeRateRequest;
import com.currency_rate.currency_rate.Dto.ExchangeRateResponse;
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
import java.util.UUID;


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

        exceptionHandlerService.validateCurrencyCode(sourceCurrency,sourceCurrency.name());
        exceptionHandlerService.validateCurrencyCode(targetCurrency,targetCurrency.name());

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
        Double convertedAmount = amount * rate;

        String message = String.format("%.2f %s is equal to %.2f %s",
                request.getAmount(),
                request.getSourceCurrency(),
                convertedAmount,
                request.getTargetCurrency());

        ConversionHistory conversionHistory = new ConversionHistory();
        conversionHistory.setExchangeRate(BigDecimal.valueOf(rate));
        conversionHistory.setSourceAmount(BigDecimal.valueOf(amount));
        conversionHistory.setTargetAmount(BigDecimal.valueOf(convertedAmount));
        conversionHistory.setSourceCurrency(request.getSourceCurrency());
        conversionHistory.setTargetCurrency(request.getTargetCurrency());
        conversionHistory.setReference(request.getSourceCurrency() + "-" + request.getTargetCurrency());
        conversionHistory.setTransactionId(UUID.randomUUID().toString());
        conversionHistory.setTransactionDate(LocalDateTime.now());
        conversionHistoryService.save(conversionHistory);

        return ConversionResponse.builder()
                .amount(BigDecimal.valueOf(convertedAmount))
                .sourceCurrency(request.getSourceCurrency())
                .targetCurrency(request.getTargetCurrency())
                .rate(BigDecimal.valueOf(rate))
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
    }
}
