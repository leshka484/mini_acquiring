package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

    @Query("""
                SELECT ME
                FROM MerchantEntity ME
                WHERE ME.name = :name
            """)
    Optional<MerchantEntity> findByName(@Param("name") String name);

    @Modifying
    @Query("""
                DELETE FROM MerchantEntity ME
                WHERE ME.name = :name
            """)
    void deleteByName(@Param("name") String name);

}
