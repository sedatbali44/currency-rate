package com.currency_rate.currency_rate.Service;

import com.currency_rate.currency_rate.Dto.*;

import java.util.List;

public interface ExchangeRateService {

    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);

    ConversionResponse calculateRateAmount(ConversionRequest request);

    void calculateConversions(ConversionsRequest request);
}