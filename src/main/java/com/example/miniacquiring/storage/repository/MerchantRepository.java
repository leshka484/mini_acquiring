package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(me) > 0 THEN true ELSE false END
            FROM MerchantEntity me
            WHERE me.id = :id
            """)
    boolean existsById(@NonNull Long id);

    @Query("""
                SELECT CASE
                    WHEN COUNT(me) = :MerchantEntity
                    THEN true
                    ELSE false
                END
                FROM MerchantEntity me
                WHERE me.id IN :ids
            """)
    boolean existsAllById(List<Long> ids);

    @Query("""
            SELECT me
            FROM MerchantEntity me
            """)
    Page<MerchantEntity> getAll(Pageable pageable);

    @Modifying
    @Query("""
                DELETE FROM MerchantEntity me
                WHERE me.id = :id
            """)
    void deleteById(@NonNull Long id);

    @Modifying
    @Query("""
                DELETE FROM MerchantEntity me
                WHERE me.id IN :ids
            """)
    void deleteAllById(List<Long> ids);

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
