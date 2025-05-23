package com.currency_rate.currency_rate.Client;

import com.currency_rate.currency_rate.Entity.Currency;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeRateProviderService {

    RestTemplate currencyConversionTemplate(RestTemplate restTemplate);

    Double convert(Currency sourceCurrency, Currency targetCurrency);

    Map<String, Double> getAllRates(Currency sourceCurrency);
}
