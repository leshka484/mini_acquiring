package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommissionTypeRepository extends JpaRepository<CommissionTypeEntity, Long> {
    Optional<CommissionTypeEntity> findByType(String type);
}
