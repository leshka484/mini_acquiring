package com.example.miniacquiring.storage.entity;

import com.example.miniacquiring.core.constant.Const;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "merchant", schema = Const.CORE_SCHEMA)
public class MerchantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @Setter
    @Column(nullable = false, name = "commission_value")
    private BigDecimal commissionValue;

    @Setter
    @ManyToOne
    @JoinColumn(name = "commission_type", nullable = false)
    private CommissionTypeEntity commissionType;
}
