package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommissionTypeRepository extends JpaRepository<CommissionTypeEntity, Long> {

    @Query("""
                SELECT CTE
                FROM CommissionTypeEntity CTE
                WHERE CTE.type = :type
            """)
    Optional<CommissionTypeEntity> findByType(@Param("type") String type);

}
