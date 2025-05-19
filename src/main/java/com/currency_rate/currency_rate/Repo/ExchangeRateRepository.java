package com.currency_rate.currency_rate.Repo;


import com.currency_rate.currency_rate.Entity.Currency;
import com.currency_rate.currency_rate.Entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {


    Optional<ExchangeRateRepository> findBySourceCurrency(Currency sourceCurrency);

    Optional<ExchangeRateRepository> findByTargetCurrency(Currency targetCurrency);

    @Query("SELECT ex FROM ExchangeRate ex WHERE ex.timestamp BETWEEN :startTime AND :endTime")
    List<ExchangeRateRepository> findByDateRange(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);
}
