package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperationTypeRepository extends JpaRepository<OperationTypeEntity, Long> {

    @Query("""
                SELECT OTE
                FROM OperationTypeEntity OTE
                WHERE OTE.type = :type
            """)
    Optional<OperationTypeEntity> findByType(@Param("type") String type);

}
