package com.currency_rate.currency_rate.Service.Impl;

import com.currency_rate.currency_rate.Client.ExchangeRateProviderService;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateProviderService exchangeRateProviderService;

    @Override
    @Transactional
    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        log.debug("Getting exchange rate for {} to {}", request.getSourceCurrency(), request.getTargetCurrency());

        if (!Currency.isValid(String.valueOf(request.getSourceCurrency()))) {
            throw new RuntimeException("Invalid source currency: " + request.getSourceCurrency());
        }

        if (!Currency.isValid(String.valueOf(request.getTargetCurrency()))) {
            throw new RuntimeException("Invalid target currency: " + request.getTargetCurrency());
        }

        Double rate = exchangeRateProviderService.convert(request.getSourceCurrency(), request.getTargetCurrency());

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .sourceCurrency(request.getSourceCurrency())
                .targetCurrency(request.getTargetCurrency())
                .rate(BigDecimal.valueOf(rate))
                .timestamp(LocalDateTime.now())
                .build();

        exchangeRateRepository.save(exchangeRate);

        ExchangeRateResponse exchangeRateResponse = ExchangeRateResponse.builder()
                .sourceCurrency(exchangeRate.getSourceCurrency())
                .targetCurrency(exchangeRate.getTargetCurrency())
                .rate(exchangeRate.getRate())
                .timestamp(exchangeRate.getTimestamp())
                .build();

        return exchangeRateResponse;
    }
}
