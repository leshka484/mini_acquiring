package com.example.miniacquiring.service;

import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.OperationTypeStorage;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationTypeService {

    private final OperationTypeStorage operationTypeStorage;

    public OperationTypeEntity getById(Long id) {
        try {
            log.info("Getting operation type by id");
            return operationTypeStorage.findById(id);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
