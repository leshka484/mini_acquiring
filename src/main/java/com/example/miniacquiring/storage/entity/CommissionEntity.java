package com.example.miniacquiring.storage.entity;

import com.example.miniacquiring.core.constant.Const;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@Table(name = "commission", schema = Const.CORE_SCHEMA)
public class CommissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "operation_id", nullable = false, unique = true)
    private OperationEntity operation;

    @Setter
    @Column(nullable = false, name = "total_commission")
    private BigDecimal totalCommission;

    @Setter
    @Column(nullable = false, name = "processed_at")
    private LocalDateTime processedAt;

}
