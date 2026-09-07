package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommissionRepository extends JpaRepository<CommissionEntity, Long> {

    @Query("""
                SELECT CE
                FROM CommissionEntity CE
                WHERE CE.operation.id = :operationId
            """)
    Optional<CommissionEntity> findByOperationId(@Param("operationId") Long operationId);

    @Query("""
                SELECT CE
                FROM CommissionEntity CE
                WHERE CE.processedAt = :processedAt
            """)
    List<CommissionEntity> findByProcessedAt(@Param("processedAt") LocalDateTime processedAt);

    @Query("""
                SELECT CE
                FROM CommissionEntity CE
                WHERE CE.processedAt BETWEEN :from AND :to
            """)
    List<CommissionEntity> findByProcessedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}
