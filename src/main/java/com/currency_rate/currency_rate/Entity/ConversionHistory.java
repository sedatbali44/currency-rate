package com.currency_rate.currency_rate.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversion_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_currency", nullable = false)
    private Currency sourceCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_currency", nullable = false)
    private Currency targetCurrency;

    @Column(name = "source_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal sourceAmount;

    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "exchange_rate", nullable = false, precision = 19, scale = 6)
    private BigDecimal exchangeRate;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "user_ip")
    private String userIp;

    @Column(name = "reference")
    private String reference;
}