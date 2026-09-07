package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationTypeRepository extends JpaRepository<OperationTypeEntity, Long> {

    @Query("""
                SELECT ote
                FROM OperationTypeEntity ote
                WHERE ote.type = :type
            """)
    Optional<OperationTypeEntity> findByType(String type);

}
