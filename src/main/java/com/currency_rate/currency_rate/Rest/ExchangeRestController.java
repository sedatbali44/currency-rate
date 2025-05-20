package com.currency_rate.currency_rate.Rest;

import com.currency_rate.currency_rate.Dto.ConversionRequest;
import com.currency_rate.currency_rate.Dto.ConversionResponse;
import com.currency_rate.currency_rate.Dto.ExchangeRateRequest;
import com.currency_rate.currency_rate.Dto.ExchangeRateResponse;
import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ExchangeRestController {

    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);
    ConversionResponse calculateRateAmount(ConversionRequest request);
    Page<ConversionHistory> findConversions(String transactionId, LocalDateTime startDate,
                                            LocalDateTime endDate, Currency sourceCurrency,
                                            Currency targetCurrency, Pageable pageable);

}
