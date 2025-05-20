package com.currency_rate.currency_rate.Service;

import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConversionHistoryService {

    void save(ConversionHistory conversionHistory);

    Page<ConversionHistory> findConversions(
            String transactionId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Currency sourceCurrency,
            Currency targetCurrency,
            Pageable pageable);
}
