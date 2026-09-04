package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationStatusRepository extends JpaRepository<OperationStatusEntity, Long> {
    Optional<OperationStatusEntity> findByStatus(String status);
}
