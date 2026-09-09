package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.repository.BaseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseStorage<T, ID, R extends BaseRepository<T, ID>> {

    protected final R repository;

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public void deleteById(List<ID> ids, String errorText) {
        List<ID> uniqueIds = ids.stream()
                .distinct()
                .toList();
        if (!repository.existsAllById(uniqueIds)) {
            throw new IllegalArgumentException(errorText);
        }
        repository.deleteAllById(uniqueIds);
    }

    public void deleteById(ID id, String errorText) {
        if (repository.existsById(id)) {
            throw new IllegalArgumentException(errorText);
        }
        repository.deleteById(id);
    }

    public List<T> getAll() {
        return repository.getAll();
    }

}
