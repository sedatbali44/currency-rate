package com.currency_rate.currency_rate.Service.Impl;

import com.currency_rate.currency_rate.Dto.ExchangeRateRequest;
import com.currency_rate.currency_rate.Dto.ExchangeRateResponse;
import com.currency_rate.currency_rate.Entity.Currency;
import com.currency_rate.currency_rate.Entity.ExchangeRate;
import com.currency_rate.currency_rate.Repo.ExchangeRateRepository;
import com.currency_rate.currency_rate.Service.ExchangeRateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Override
    @Transactional
    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        log.debug("Getting exchange rate for {} to {}", request.getSourceCurrency(), request.getTargetCurrency());

        if (!Currency.isValid(request.getSourceCurrency())) {
            throw new RuntimeException("Invalid source currency: " + request.getSourceCurrency());
        }

        if (!Currency.isValid(request.getTargetCurrency())) {
            throw new RuntimeException("Invalid target currency: " + request.getTargetCurrency());
        }

        Currency sourceCurrency = Currency.valueOf(request.getSourceCurrency().toUpperCase());
        Currency targetCurrency = Currency.valueOf(request.getTargetCurrency().toUpperCase());

        ExchangeRateResponse response = null;// exchangeRateProvider.getExchangeRate(sourceCurrency, targetCurrency);

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .rate(response.getRate())
                .timestamp(LocalDateTime.now())
                .build();

        exchangeRateRepository.save(exchangeRate);

        return response;
    }
}
