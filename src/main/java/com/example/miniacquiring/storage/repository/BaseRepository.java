package com.example.miniacquiring.storage.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BaseRepository<T, ID> extends JpaRepository<T,ID> {
    @Query("""
                SELECT CASE
                    WHEN COUNT(e) = :#{#ids.size()}
                    THEN true
                    ELSE false
                END
                FROM #{#entityName} e
                WHERE e.id IN :ids
            """)
    boolean existsAllById(List<ID> ids);

    @Modifying
    @Query("""
                DELETE FROM #{#entityName} e
                WHERE e.id IN :ids
            """)
    void deleteAllById(List<ID> ids);
}
