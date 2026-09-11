package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommissionTypeRepository extends JpaRepository<CommissionTypeEntity, Long>{

    @Query("""
                SELECT cte
                FROM CommissionTypeEntity cte
                WHERE cte.type = :type
            """)
    Optional<CommissionTypeEntity> findByType(String type);

}
