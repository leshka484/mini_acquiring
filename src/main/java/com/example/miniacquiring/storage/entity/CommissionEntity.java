package com.example.miniacquiring.storage.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "commission", schema = "core")
public class CommissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "operation_id", nullable = false, unique = true)
    private OperationEntity operation; // Связанная операция

    @Column(nullable = false, name = "total_commission")
    private BigDecimal totalCommission; // Комиссия по операции

    @Column(nullable = false, name = "processed_at")
    private Date processedAt; // Дата расчета комиссии
}
