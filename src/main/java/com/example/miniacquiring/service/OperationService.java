package com.example.miniacquiring.service;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.CreateOperationRequest;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.UpdateOperationRequest;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStatusStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.OperationTypeStorage;
import com.example.miniacquiring.storage.entity.OperationEntity;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationService {

    private final OperationStorage operationStorage;
    private final OperationTypeStorage operationTypeStorage;
    private final OperationStatusStorage operationStatusStorage;
    private final MerchantStorage merchantStorage;
    private final DtoMapper dtoMapper;

    public void create(@Valid @RequestBody CreateOperationRequest request) {
        log.info("Creating operation");
        createOperationEntity(request);
    }

    public GetOperationResponse getById(Long id) {
        try {
            log.info("Trying to find operation with id = {}", id);
            var operation = operationStorage.findById(id);
            return dtoMapper.toResponse(operation);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public Page<GetOperationResponse> getAll(Pageable pageable) {
        log.info("Getting all operations");
        var operations = operationStorage.findAll(pageable);
        return operations.map(dtoMapper::toResponse);
    }

    public void update(Long id, UpdateOperationRequest request) {
        try {
            log.info("Updating operation with id = {}", id);
            operationStorage.findById(id);
            updateOperationEntity(id, request);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public void deleteById(List<Long> ids) {
        log.info("Deleting some operations");
        operationStorage.deleteById(ids);
    }

    public void deleteById(Long id) {
        log.info("Deleting operation");
        operationStorage.deleteById(id);
    }

    private void createOperationEntity(CreateOperationRequest request) {
        var type = operationTypeStorage.findById(request.typeId());
        var status = operationStatusStorage.findById(request.statusId());
        var merchant = merchantStorage.findById(request.merchantId());
        var operation = OperationEntity
                .builder()
                .merchant(merchant)
                .status(status)
                .sum(request.sum())
                .type(type)
                .parentId(request.parentId())
                .createdAt(request.createdAt())
                .processedAt(request.processedAt())
                .build();
        operationStorage.save(operation);
    }

    private void updateOperationEntity(Long id, UpdateOperationRequest request) {
        var type = operationTypeStorage.findById(request.typeId());
        var status = operationStatusStorage.findById(request.statusId());
        var operation = OperationEntity
                .builder()
                .id(id)
                .status(status)
                .type(type)
                .parentId(request.parentId())
                .processedAt(request.processedAt())
                .build();
        operationStorage.save(operation);
    }

}
