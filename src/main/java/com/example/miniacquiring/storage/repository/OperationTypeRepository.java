package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationTypeRepository extends JpaRepository<OperationTypeEntity, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(ote) > 0 THEN true ELSE false END
            FROM OperationTypeEntity ote
            WHERE ote.id = :id
            """)
    boolean existsById(@NonNull Long id);

    @NonNull
    @Query("""
                SELECT ote
                FROM OperationTypeEntity ote
                WHERE ote.id = :id
            """)
    Optional<OperationTypeEntity> findById(@NonNull Long id);

    @Query("""
                SELECT ote
                FROM OperationTypeEntity ote
                WHERE ote.type = :type
            """)
    Optional<OperationTypeEntity> findByType(String type);

}
