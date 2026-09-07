package com.example.miniacquiring.storage.entity;

import com.example.miniacquiring.core.constant.Const;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Entity
@Table(name = "merchant", schema = Const.CORE_SCHEMA)
public class MerchantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, name = "commission_value")
    private BigDecimal commissionValue;

    @ManyToOne
    @JoinColumn(name = "commission_type", nullable = false)
    private CommissionTypeEntity commissionType;

}
