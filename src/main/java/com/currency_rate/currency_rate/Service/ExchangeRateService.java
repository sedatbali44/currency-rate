package com.currency_rate.currency_rate.Service;

import com.currency_rate.currency_rate.Dto.ConversionRequest;
import com.currency_rate.currency_rate.Dto.ConversionResponse;
import com.currency_rate.currency_rate.Dto.ExchangeRateResponse;
import com.currency_rate.currency_rate.Dto.ExchangeRateRequest;

public interface ExchangeRateService {

    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);

    ConversionResponse calculateRateAmount(ConversionRequest request);
}