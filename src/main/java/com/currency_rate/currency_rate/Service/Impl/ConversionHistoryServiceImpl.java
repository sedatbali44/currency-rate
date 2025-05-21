package com.currency_rate.currency_rate.Service.Impl;


import com.currency_rate.currency_rate.Dto.ConversionRequest;
import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import com.currency_rate.currency_rate.Repo.ConversionHistoryRepository;
import com.currency_rate.currency_rate.Service.ConversionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversionHistoryServiceImpl implements ConversionHistoryService {

    @Autowired
    private ConversionHistoryRepository repo;


    @Override
    public Optional<ConversionHistory> findByTransactionId(String transactionId) {
        return repo.findByTransactionId(transactionId);
    }

    @Override
    public void saveAll(List<ConversionHistory> conversionHistories) {
        repo.saveAll(conversionHistories);
    }

    @Override
    public ConversionHistory createConversionHistory(ConversionRequest request, BigDecimal rate,
                                                     BigDecimal sourceAmount, BigDecimal targetAmount) {

        ConversionHistory conversionHistory = new ConversionHistory();

        conversionHistory.setExchangeRate(rate);
        conversionHistory.setSourceAmount(sourceAmount);
        conversionHistory.setTargetAmount(targetAmount);
        conversionHistory.setSourceCurrency(request.getSourceCurrency());
        conversionHistory.setTargetCurrency(request.getTargetCurrency());
        conversionHistory.setReference(request.getSourceCurrency() + "-" + request.getTargetCurrency());
        conversionHistory.setTransactionId(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        conversionHistory.setTransactionDate(LocalDateTime.now());

        repo.save(conversionHistory);
        return conversionHistory;
    }

    @Override
    public Page<ConversionHistory> findConversions(String transactionId, LocalDateTime startDate,
                                                   LocalDateTime endDate, Currency sourceCurrency,
                                                   Currency targetCurrency, Pageable pageable) {
        return repo.findConversions(
                sourceCurrency,
                targetCurrency,
                transactionId,
                startDate,
                endDate,
                pageable);
    }
}
