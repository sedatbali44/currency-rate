package com.currency_rate.currency_rate.Service;

import com.currency_rate.currency_rate.Dto.ConversionRequest;
import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ConversionHistoryService {

    ConversionHistory createConversionHistory(ConversionRequest request,
                                             BigDecimal rate,
                                             BigDecimal sourceAmount,
                                             BigDecimal targetAmount);

    Page<ConversionHistory> findConversions(
            String transactionId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Currency sourceCurrency,
            Currency targetCurrency,
            Pageable pageable);
}
