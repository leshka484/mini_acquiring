package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

    @Query("""
                SELECT me
                FROM MerchantEntity me
                WHERE me.name = :name
            """)
    Optional<MerchantEntity> findByName(String name);

    @Modifying
    @Query("""
                DELETE FROM MerchantEntity me
                WHERE me.name = :name
            """)
    void deleteByName(String name);

}
