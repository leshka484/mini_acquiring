package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface MerchantRepository extends BaseRepository<MerchantEntity, Long> {

    @Query("""
                SELECT me
                FROM MerchantEntity me
                WHERE me.name = :name
            """)
    Optional<MerchantEntity> findByName(String name);

    @Query("""
                SELECT CASE WHEN COUNT(me) > 0 THEN true ELSE false END
                FROM MerchantEntity me
                WHERE me.name = :name
            """)
    boolean existsByName(String name);

}
