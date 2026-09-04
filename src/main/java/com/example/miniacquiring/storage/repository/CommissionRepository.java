package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommissionRepository extends JpaRepository<CommissionEntity, Long> {
    Optional<CommissionEntity> findByOperation(OperationEntity operation);
    List<CommissionEntity> findByProcessedAt(LocalDateTime processedAt);
    List<CommissionEntity> findByProcessedAtBetween(LocalDateTime from, LocalDateTime to);
}
