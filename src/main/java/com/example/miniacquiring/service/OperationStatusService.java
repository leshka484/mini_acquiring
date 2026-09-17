package com.example.miniacquiring.service;

import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.OperationStatusStorage;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationStatusService {

    private final OperationStatusStorage operationStatusStorage;

    public OperationStatusEntity getById(Long id) {
        try {
            log.info("Getting operation status by id");
            return operationStatusStorage.findById(id);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
