package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MerchantStatusRepository extends JpaRepository<MerchantStatusEntity, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(mse) > 0 THEN true ELSE false END
            FROM MerchantStatusEntity mse
            WHERE mse.id = :id
            """)
    boolean existsById(@NonNull Long id);

    @NonNull
    @Query("""
                SELECT mse
                FROM MerchantStatusEntity mse
                WHERE mse.id = :id
            """)
    Optional<MerchantStatusEntity> findById(@NonNull Long id);

}
