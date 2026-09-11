package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.OperationEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
            """)
    List<OperationEntity> findByMerchant(Long merchantId);

    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.status.id = :status
            """)
    List<OperationEntity> findByStatus(Long status);

    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.type.id = :type
            """)
    List<OperationEntity> findByType(Long type);

    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.createdAt = :date
            """)
    List<OperationEntity> findByCreatedAt(LocalDateTime date);

    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.createdAt BETWEEN :from AND :to
            """)
    List<OperationEntity> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.processedAt = :processedAt
            """)
    List<OperationEntity> findByProcessedAt(LocalDateTime processedAt);

    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.processedAt BETWEEN :from AND :to
            """)
    List<OperationEntity> findByProcessedAtBetween(LocalDateTime from, LocalDateTime to);

    @Query("""
                SELECT oe
                FROM OperationEntity oe
                WHERE oe.parentId = :parentId
            """)
    Optional<OperationEntity> findByParentId(Long parentId);

    @Query("""
                SELECT COUNT(oe)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
            """)
    Long countMerchantOperations(Long merchantId);

    @Query("""
                SELECT COALESCE(SUM(sum), 0)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
            """)
    BigDecimal sumMerchantOperations(Long id);

    @Query("""
                SELECT COUNT(oe)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
                AND oe.createdAt BETWEEN :from AND :to
            """)
    Long countMerchantOperationsBetween(Long merchantId, LocalDateTime from, LocalDateTime to);

    @Query("""
                SELECT COALESCE(SUM(sum), 0)
                FROM OperationEntity oe
                WHERE oe.merchant.id = :merchantId
                AND oe.createdAt BETWEEN :from AND :to
            """)
    BigDecimal sumMerchantOperationsBetween(Long merchantId, LocalDateTime from, LocalDateTime to);

}
