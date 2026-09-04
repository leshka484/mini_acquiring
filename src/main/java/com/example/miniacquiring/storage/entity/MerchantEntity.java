package com.example.miniacquiring.storage.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "merchant", schema = "core")
public class MerchantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Название мерчанта

    @Column(nullable = false, name = "commission_value")
    private BigDecimal commissionValue; // Процент\ставка комиссии

    @ManyToOne
    @JoinColumn(name = "commission_type", nullable = false)
    private CommissionTypeEntity commissionType; // Тип комиссии
}
