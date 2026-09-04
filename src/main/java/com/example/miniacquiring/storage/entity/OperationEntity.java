package com.example.miniacquiring.storage.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "operation", schema = "core")
public class OperationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private MerchantEntity merchant; //Связанный мерчант

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private OperationStatusEntity status; //Статус операции

    @Column(nullable = false)
    private BigDecimal sum; //Сумма по операции

    @ManyToOne
    @JoinColumn(name = "operation_type_id", nullable = false)
    private OperationTypeEntity type; // Тип операции

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false, name = "created_at")
    private Date createdAt; // Дата создания

    @Column(name = "processed_at")
    private Date processedAt; // Дата проведения

    // тип (оплата и возврат) +
    // parentId для ссылки на прошлую операцию для операций возврата +
    // dbSchema +
    // sql и файл dbSChema +-
    // залить на git
}
