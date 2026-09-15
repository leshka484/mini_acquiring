package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class OperationTypeStorage {

    private OperationTypeRepository operationTypeRepository;

    public OperationTypeEntity findById(Long id) {
        return operationTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation type with id = %d not found".formatted(id))
        );
    }

}
