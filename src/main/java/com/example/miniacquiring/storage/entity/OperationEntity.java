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
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Entity
@Table(name = "operation", schema = Const.CORE_SCHEMA)
public class OperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private MerchantEntity merchant;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private OperationStatusEntity status;

    @Column(nullable = false)
    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "operation_type_id", nullable = false)
    private OperationTypeEntity type;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

}
