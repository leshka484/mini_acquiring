package com.example.miniacquiring.storage.entity;

import com.example.miniacquiring.core.Const;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Builder(toBuilder = true)
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "operation", schema = Const.CORE_SCHEMA)
public class OperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Generated(event = EventType.INSERT)
    @Column(name = "public_id", nullable = false, unique = true, insertable = false, updatable = false)
    private UUID publicId;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private MerchantEntity merchant;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private OperationStatusEntity status;

    @Column(name = "sum", nullable = false)
    private Long sum;

    @ManyToOne
    @JoinColumn(name = "operation_type_id", nullable = false)
    private OperationTypeEntity type;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

}
