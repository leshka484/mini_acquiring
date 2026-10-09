package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.core.enums.MerchantStatus;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long>,
        JpaSpecificationExecutor<MerchantEntity> {

    @Query("""
            SELECT CASE WHEN COUNT(me) > 0 THEN true ELSE false END
            FROM MerchantEntity me
            WHERE me.id = :id
            """)
    boolean existsById(@NonNull Long id);

    @NonNull
    @Query("""
            SELECT me
            FROM MerchantEntity me
            """)
    Page<MerchantEntity> findAll(@NonNull Pageable pageable);

    @Query("""
            SELECT me
            FROM MerchantEntity me
            WHERE me.publicId = :publicId
            """)
    Optional<MerchantEntity> findByPublicId(UUID publicId);

    @Modifying
    @Query("""
                DELETE FROM MerchantEntity me
                WHERE me.id IN :ids
            """)
    void deleteAllById(List<Long> ids);

    @Query("""
            SELECT me.status.code = :status
            FROM MerchantEntity me
            WHERE me.id = :id
            """)
    boolean isActive(Long id, MerchantStatus status);

}
