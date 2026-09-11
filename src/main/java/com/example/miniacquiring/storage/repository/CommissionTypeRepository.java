package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommissionTypeRepository extends JpaRepository<CommissionTypeEntity, Long> {

    @NonNull
    @Query("""
                SELECT cte
                FROM CommissionTypeEntity cte
                WHERE cte.id = :id
            """)
    Optional<CommissionTypeEntity> findById(@NonNull Long id);

    @Query("""
                SELECT cte
                FROM CommissionTypeEntity cte
                WHERE cte.type = :type
            """)
    Optional<CommissionTypeEntity> findByType(String type);

}
