package com.currency_rate.currency_rate.Repo;


import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long> {

    Optional<ConversionHistory> findByTransactionId(String transactionId);

    @Query("SELECT c FROM ConversionHistory c " +
            "WHERE (:sourceCurrency IS NULL OR c.sourceCurrency = :sourceCurrency) " +
            "AND (:targetCurrency IS NULL OR c.targetCurrency = :targetCurrency) " +
            "AND (:transactionId IS NULL OR c.transactionId = :transactionId) " +
            "AND (:startDate IS NULL OR c.transactionDate >= :startDate) " +
            "AND (:endDate IS NULL OR c.transactionDate <= :endDate)")
    Page<ConversionHistory> findConversions(
            @Param("sourceCurrency") Currency sourceCurrency,
            @Param("targetCurrency") Currency targetCurrency,
            @Param("transactionId") String transactionId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);


}
