package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationStatusRepository extends JpaRepository<OperationStatusEntity, Long> {

    @Query("""
                SELECT ose
                FROM OperationStatusEntity ose
                WHERE ose.status = :status
            """)
    Optional<OperationStatusEntity> findByStatus(String status);

}
