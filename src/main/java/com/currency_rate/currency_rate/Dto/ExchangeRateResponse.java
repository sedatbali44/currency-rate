package com.currency_rate.currency_rate.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {

    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal rate;
    private LocalDateTime timestamp;
}