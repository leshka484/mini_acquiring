package com.example.miniacquiring.storage.repository;

import com.example.miniacquiring.storage.entity.OperationEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    @Query("""
                SELECT OE
                FROM OperationEntity OE
                WHERE OE.merchant.id = :merchantId
            """)
    List<OperationEntity> findByMerchant(@Param("merchantId") Long merchantId);

    @Query("""
                SELECT OE
                FROM OperationEntity OE
                WHERE OE.status.id = status
            """)
    List<OperationEntity> findByStatus(@Param("status") Long status);

    @Query("""
                SELECT OE
                FROM OperationEntity OE
                WHERE OE.type.id = type
            """)
    List<OperationEntity> findByType(@Param("type") Long type);

    @Query("""
                SELECT OE
                FROM OperationEntity OE
                WHERE OE.createdAt = :date
            """)
    List<OperationEntity> findByCreatedAt(@Param("date") LocalDateTime date);

    @Query("""
                SELECT OE
                FROM OperationEntity OE
                WHERE OE.createdAt BETWEEN :from AND :to
            """)
    List<OperationEntity> findByCreatedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
                SELECT OE
                FROM OperationEntity OE
                WHERE OE.processedAt = :processedAt
            """)
    List<OperationEntity> findByProcessedAt(@Param("processedAt") LocalDateTime processedAt);

    @Query("""
                SELECT OE
                FROM OperationEntity OE
                WHERE OE.processedAt BETWEEN :from AND :to
            """)
    List<OperationEntity> findByProcessedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
                SELECT OE
                FROM OperationEntity OE
                WHERE OE.parentId = :parentId
            """)
    Optional<OperationEntity> findByParentId(@Param("parentId") Long parentId);

}
