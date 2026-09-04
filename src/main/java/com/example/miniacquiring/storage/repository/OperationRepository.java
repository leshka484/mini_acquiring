package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {
    List<OperationEntity> findByMerchant(MerchantEntity merchant);
    List<OperationEntity> findByStatus(OperationStatusEntity status);
    List<OperationEntity> findByType(OperationTypeEntity type);
    List<OperationEntity> findByCreatedAt(LocalDateTime date);
    List<OperationEntity> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<OperationEntity> findByProcessedAt(LocalDateTime processedAt);
    List<OperationEntity> findByProcessedAtBetween(LocalDateTime from, LocalDateTime to);
    Optional<OperationEntity> findByParentId(Long id);

}
