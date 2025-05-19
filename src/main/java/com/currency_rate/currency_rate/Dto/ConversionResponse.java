package com.currency_rate.currency_rate.Dto;

import com.currency_rate.currency_rate.Entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResponse {

    private Currency sourceCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private LocalDateTime timestamp;
    private String message;
    private BigDecimal amount;
}
