package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.core.OperationStatusEnum;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    @NonNull
    @Query("""
            SELECT oe
            FROM OperationEntity oe
            """)
    Page<OperationEntity> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {
            "merchant",
            "merchant.commissionType"
    })
    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.status.code = :code
            """)
    List<OperationEntity> findByStatus(OperationStatusEnum code);

    @Modifying
    @Query("""
                 UPDATE OperationEntity oe
                 SET oe.status = (
                            SELECT ose
                            FROM OperationStatusEntity ose
                            WHERE ose.code = :completedCode)
                 WHERE oe.status = (
                            SELECT ose
                            FROM OperationStatusEntity ose
                            WHERE ose.code = :paidCode)
            """)
    void completeAllPaid(OperationStatusEnum paidCode, OperationStatusEnum completedCode);

    @Modifying
    @Query("""
                UPDATE OperationEntity oe
                SET oe.status = :status,
                    oe.processedAt = :processedAt
                WHERE oe.id = :id
            """)
    void processOperation(Long id, OperationStatusEntity status, LocalDateTime processedAt);

    @Modifying
    @Query("""
                DELETE FROM OperationEntity oe
                WHERE oe.id IN :ids
            """)
    void deleteAllById(List<Long> ids);

    @Query("""
                SELECT COUNT(oe)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
            """)
    Long countMerchantOperations(Long merchantId);

    @Query("""
                SELECT COALESCE(SUM(oe.sum), 0)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
            """)
    BigDecimal sumMerchantOperations(Long merchantId);

    @Query("""
                SELECT COUNT(oe)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
                AND oe.createdAt BETWEEN :from AND :to
            """)
    Long countMerchantOperationsBetween(Long merchantId, LocalDateTime from, LocalDateTime to);

    @Query("""
                SELECT COALESCE(SUM(oe.sum), 0)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
                AND oe.createdAt BETWEEN :from AND :to
            """)
    BigDecimal sumMerchantOperationsBetween(Long merchantId, LocalDateTime from, LocalDateTime to);

}
