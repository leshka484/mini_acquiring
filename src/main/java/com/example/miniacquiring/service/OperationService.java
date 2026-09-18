package com.example.miniacquiring.service;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.UpsertOperationRequest;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
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
    private final OperationTypeRepository operationTypeRepository;
    private final OperationStatusRepository operationStatusRepository;
    private final MerchantStorage merchantStorage;
    private final DtoMapper dtoMapper;

    public void create(@Valid @RequestBody UpsertOperationRequest request) {
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

    @Transactional
    public void processOperation(Long id) {
        try {
            operationStorage.processOperation(id);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public void update(Long id, UpsertOperationRequest request) {
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

    private void createOperationEntity(UpsertOperationRequest request) {
        var type = getOperationType(request.typeId());
        var status = getOperationStatus(request.statusId());
        var merchant = merchantStorage.findById(request.merchantId());
        var operation = OperationEntity
                .builder()
                .merchant(merchant)
                .status(status)
                .sum(request.sum())
                .type(type)
                .parentId(request.parentId())
                .createdAt(LocalDateTime.now())
                .build();
        operationStorage.save(operation);
    }

    private void updateOperationEntity(Long id, UpsertOperationRequest request) {
        var type = getOperationType(request.typeId());
        var status = getOperationStatus(request.statusId());
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

    private OperationStatusEntity getOperationStatus(Long id) {
        return operationStatusRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation status with id = %d not found".formatted(id))
        );
    }

    private OperationTypeEntity getOperationType(Long id) {
        return operationTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation type with id = %d not found".formatted(id))
        );
    }

}
