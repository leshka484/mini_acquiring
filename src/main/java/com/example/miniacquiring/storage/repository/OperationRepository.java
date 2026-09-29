package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.core.enums.OperationStatus;
import com.example.miniacquiring.storage.entity.OperationEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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
    List<OperationEntity> findByStatus(OperationStatus code);

    @Query("""
                SELECT COUNT(oe)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
                AND oe.status.code = :status
            """)
    Long countMerchantOperations(Long merchantId, OperationStatus status);

    @Query("""
                SELECT COALESCE(SUM(oe.sum), 0)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
                AND oe.status.code = :status
            """)
    Long sumMerchantOperations(Long merchantId, OperationStatus status);

    @Query("""
                SELECT COUNT(oe)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
                AND oe.status.code = :status
                AND oe.createdAt BETWEEN :from AND :to
            """)
    Long countMerchantOperationsBetween(Long merchantId, OperationStatus status, LocalDateTime from, LocalDateTime to);

    @Query("""
                SELECT COALESCE(SUM(oe.sum), 0)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
                AND oe.status.code = :status
                AND oe.createdAt BETWEEN :from AND :to
            """)
    Long sumMerchantOperationsBetween(Long merchantId, OperationStatus status, LocalDateTime from, LocalDateTime to);

}
