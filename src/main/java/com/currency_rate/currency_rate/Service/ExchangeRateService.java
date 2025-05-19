package com.currency_rate.currency_rate.Service;

import com.currency_rate.currency_rate.Dto.ExchangeRateResponse;
import com.currency_rate.currency_rate.Dto.ExchangeRateRequest;

public interface ExchangeRateService {

    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);
}