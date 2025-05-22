package com.currency_rate.currency_rate.Client.Impl;

import com.currency_rate.currency_rate.Client.Dto.ExchangeRateClientResponse;
import com.currency_rate.currency_rate.Client.ExchangeRateProviderService;
import com.currency_rate.currency_rate.Entity.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;


@Service
@RequiredArgsConstructor
@EnableCaching
@Slf4j
public class ExchangeRateProviderServiceImpl implements ExchangeRateProviderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheManager cacheManager;

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/838bec4c5914d4d065dab55a/latest/";
    private static final String SUCCESS_RESPONSE = "success";
    private static final String ALL_RATES_CACHE_KEY = "exchangeRates";

    @Override
    public RestTemplate currencyConversionTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        return restTemplate;
    }

    @Override
    public Double convert(Currency sourceCurrency, Currency targetCurrency) {

        ExchangeRateClientResponse response = sendExternalRequest(sourceCurrency);

        if (response != null && SUCCESS_RESPONSE.equals(response.getResult())) {
            Double rate = response.getConversion_rates().get(targetCurrency.name());
            if (rate != null) {
                return rate;
            } else {
                throw new IllegalArgumentException("Invalid target currency: " + targetCurrency);
            }
        } else {
            throw new RuntimeException("Failed to retrieve exchange rates");
        }
    }

    @Override
    public Map<String, Double> getAllRates(Currency sourceCurrency) {
        Cache cache = cacheManager.getCache(ALL_RATES_CACHE_KEY);
        String cacheKey = sourceCurrency.name();

        Cache.ValueWrapper cachedValue = cache.get(cacheKey);
        if (cachedValue != null) {
            return (Map<String, Double>) cachedValue.get();
        }

        Map<String, Double> rates = sendExternalRequest(sourceCurrency).getConversion_rates();
        cache.put(cacheKey, rates);
        return rates;
    }

    private ExchangeRateClientResponse sendExternalRequest(Currency sourceCurrency) {
        String url = API_URL + sourceCurrency;
        ExchangeRateClientResponse response = restTemplate.getForObject(url, ExchangeRateClientResponse.class);
        return response;
    }

}
