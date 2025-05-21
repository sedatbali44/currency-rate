package com.currency_rate.currency_rate.Rest;

import com.currency_rate.currency_rate.Dto.*;
import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExchangeRestController {

    ResponseEntity<String> uploadCSV(MultipartFile file);
    Optional<ConversionHistory> findByTransactionId(String transactionId);
    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);
    ConversionResponse calculateRateAmount(ConversionRequest request);
    Page<ConversionHistory> findConversions(String transactionId, LocalDateTime startDate,
                                            LocalDateTime endDate, Currency sourceCurrency,
                                            Currency targetCurrency, Pageable pageable);

}
