package com.currency_rate.currency_rate.Rest;

import com.currency_rate.currency_rate.Dto.ConversionRequest;
import com.currency_rate.currency_rate.Dto.ConversionResponse;
import com.currency_rate.currency_rate.Dto.ExchangeRateRequest;
import com.currency_rate.currency_rate.Dto.ExchangeRateResponse;

public interface ExchangeRestController {

    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);
    ConversionResponse calculateRateAmount(ConversionRequest request);

}
