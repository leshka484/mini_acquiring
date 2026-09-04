package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationTypeRepository extends JpaRepository<OperationTypeEntity, Long> {
    Optional<OperationTypeEntity> findByType(String type);
}
