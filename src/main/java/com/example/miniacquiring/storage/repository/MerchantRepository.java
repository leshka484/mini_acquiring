package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {
    Optional<MerchantEntity> findByName(String name);
    void deleteByName(String name);
}
