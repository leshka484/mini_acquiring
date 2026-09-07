package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommissionRepository extends JpaRepository<CommissionEntity, Long> {

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

}
