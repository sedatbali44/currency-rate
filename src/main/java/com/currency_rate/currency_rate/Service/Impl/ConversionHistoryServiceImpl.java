package com.currency_rate.currency_rate.Service.Impl;


import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import com.currency_rate.currency_rate.Repo.ConversionHistoryRepository;
import com.currency_rate.currency_rate.Service.ConversionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConversionHistoryServiceImpl implements ConversionHistoryService {

    @Autowired
    private ConversionHistoryRepository repo;


    @Override
    public void save(ConversionHistory conversionHistory) {
        repo.save(conversionHistory);
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
