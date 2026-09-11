package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    @Query("""
                SELECT CASE
                    WHEN COUNT(oe) = :OperationEntity
                    THEN true
                    ELSE false
                END
                FROM OperationEntity oe
                WHERE oe.id IN :ids
            """)
    boolean existsAllById(List<Long> ids);

    @NonNull
    @Query("""
            SELECT oe
            FROM OperationEntity oe
            """)
    Page<OperationEntity> findAll(@NonNull Pageable pageable);

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

    @Modifying
    @Query("""
                DELETE FROM OperationEntity oe
                WHERE oe.id = :id
            """)
    void deleteById(@NonNull Long id);

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
