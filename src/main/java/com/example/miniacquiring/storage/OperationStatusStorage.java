package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.OperationStatusEnum;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OperationStatusStorage {

    private final OperationStatusRepository operationStatusRepository;

    public OperationStatusEntity findById(Long id) {
        return operationStatusRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation status with id = %d not found".formatted(id))
        );
    }

    public OperationStatusEntity findByCode(OperationStatusEnum code) {
        return operationStatusRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException("Operation status with code = %s not found".formatted(code))
        );
    }

}
