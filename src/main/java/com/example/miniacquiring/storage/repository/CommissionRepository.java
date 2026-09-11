package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommissionRepository extends JpaRepository<CommissionEntity, Long> {

    @Query("""
                SELECT CASE
                    WHEN COUNT(ce) = :CommissionEntity
                    THEN true
                    ELSE false
                END
                FROM CommissionEntity ce
                WHERE ce.id IN :ids
            """)
    boolean existsAllById(List<Long> ids);

    @Query("""
                SELECT ce
                FROM CommissionEntity ce
                WHERE ce.operation.id = :operationId
            """)
    Optional<CommissionEntity> findByOperationId(Long operationId);

    @Query("""
                SELECT ce
                FROM CommissionEntity ce
                WHERE ce.processedAt = :processedAt
            """)
    List<CommissionEntity> findByProcessedAt(LocalDateTime processedAt);

    @Query("""
                SELECT ce
                FROM CommissionEntity ce
                WHERE ce.processedAt BETWEEN :from AND :to
            """)
    List<CommissionEntity> findByProcessedAtBetween(LocalDateTime from, LocalDateTime to);

    @Modifying
    @Query("""
                DELETE FROM CommissionEntity ce
                WHERE ce.id = :id
            """)
    void deleteById(@NonNull Long id);

    @Modifying
    @Query("""
                DELETE FROM CommissionEntity ce
                WHERE ce.id IN :ids
            """)
    void deleteAllById(List<Long> ids);

    @Query("""
                SELECT COUNT(ce)
                FROM CommissionEntity ce
                WHERE ce.operation.merchant.id = :merchantId
            """)
    Long countMerchantCommissions(Long merchantId);

    @Query("""
            SELECT COALESCE(SUM(ce.operation.sum), 0)
            FROM CommissionEntity ce
            WHERE ce.operation.merchant.id = :merchantId
            """)
    BigDecimal sumMerchantCommissions(Long id);

    @Query("""
                SELECT COUNT(ce)
                FROM CommissionEntity ce
                WHERE ce.operation.merchant.id = :merchantId
                AND ce.processedAt BETWEEN :from AND :to
            """)
    Long countMerchantCommissionsBetween(Long merchantId, LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT COALESCE(SUM(ce.operation.sum), 0)
            FROM CommissionEntity ce
            WHERE ce.operation.merchant.id = :merchantId
            AND ce.processedAt BETWEEN :from AND :to
            """)
    BigDecimal sumMerchantCommissionsBetween(Long id, LocalDateTime from, LocalDateTime to);

}
