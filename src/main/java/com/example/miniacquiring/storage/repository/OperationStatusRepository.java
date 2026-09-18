package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.core.OperationStatusEnum;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationStatusRepository extends JpaRepository<OperationStatusEntity, Long> {

    @Query("""
                SELECT CASE WHEN COUNT(ose) > 0 THEN true ELSE false END
                FROM OperationStatusEntity ose
                WHERE ose.id = :id
            """)
    boolean existsById(@NonNull Long id);

    @Query("""
                SELECT ose
                FROM OperationStatusEntity ose
                WHERE ose.code = :code
            """)
    Optional<OperationStatusEntity> findByCode(OperationStatusEnum code);

    @NonNull
    @Query("""
                SELECT ose
                FROM OperationStatusEntity ose
                WHERE ose.id = :id
            """)
    Optional<OperationStatusEntity> findById(@NonNull Long id);

}
