package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommissionRepository extends JpaRepository<CommissionEntity, Long> {

    @Query("""
                SELECT ce
                FROM CommissionEntity ce
                WHERE ce.id = :id
            """)
    @NonNull
    Optional<CommissionEntity> findById(@NonNull Long id);

    @Query("""
                SELECT COUNT(ce)
                FROM CommissionEntity ce
                WHERE ce.operation.merchant.id = :merchantId
            """)
    Long countMerchantCommissions(Long merchantId);

    @Query("""
            SELECT COALESCE(SUM(ce.totalCommission), 0)
            FROM CommissionEntity ce
            WHERE ce.operation.merchant.id = :merchantId
            """)
    BigDecimal sumMerchantCommissions(Long merchantId);

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
    BigDecimal sumMerchantCommissionsBetween(Long merchantId, LocalDateTime from, LocalDateTime to);

}
