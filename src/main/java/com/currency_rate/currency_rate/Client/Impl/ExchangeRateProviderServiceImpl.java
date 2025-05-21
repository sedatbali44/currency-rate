package com.currency_rate.currency_rate.Client.Impl;

import com.currency_rate.currency_rate.Client.Dto.ExchangeRateClientResponse;
import com.currency_rate.currency_rate.Client.ExchangeRateProviderService;
import com.currency_rate.currency_rate.Entity.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateProviderServiceImpl implements ExchangeRateProviderService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/838bec4c5914d4d065dab55a/latest/";
    private static final String SUCCESS_RESPONSE = "success";

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
        ExchangeRateClientResponse response = sendExternalRequest(sourceCurrency);
        return response.getConversion_rates();
    }

    private ExchangeRateClientResponse sendExternalRequest(Currency sourceCurrency) {
        String url = API_URL + sourceCurrency;
        ExchangeRateClientResponse response = restTemplate.getForObject(url, ExchangeRateClientResponse.class);
        return response;
    }
}
