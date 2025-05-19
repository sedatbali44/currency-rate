package com.currency_rate.currency_rate.Repo;


import com.currency_rate.currency_rate.Entity.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {

    Optional<Conversion> findByTransactionId(String transactionId);

    @Query("SELECT c FROM Conversion c WHERE " +
            "(:transactionId IS NULL OR c.transactionId = :transactionId) AND " +
            "(:startDate IS NULL OR c.transactionDate >= :startDate) AND " +
            "(:endDate IS NULL OR c.transactionDate <= :endDate)")
    Page<Conversion> findConversions(
            @Param("transactionId") String transactionId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
