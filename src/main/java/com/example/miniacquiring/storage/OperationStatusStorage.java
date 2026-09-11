package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import org.springframework.stereotype.Component;

@Component
public class OperationStatusStorage {

    private OperationStatusRepository operationStatusRepository;

    public OperationStatusEntity findById(Long id) {
        return operationStatusRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Operation status with id = %d not found".formatted(id))
        );
    }

}
