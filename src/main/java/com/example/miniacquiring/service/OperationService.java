package com.example.miniacquiring.service;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.PayRequest;
import com.example.miniacquiring.core.dto.UpsertOperationRequest;
import com.example.miniacquiring.core.enums.MerchantStatus;
import com.example.miniacquiring.core.enums.OperationStatus;
import com.example.miniacquiring.core.exception.BadRequestException;
import com.example.miniacquiring.core.exception.EntityInvalidStatus;
import com.example.miniacquiring.core.exception.EntityNotActiveException;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.EntityNotVerifiedException;
import com.example.miniacquiring.core.exception.ForbiddenException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationService {

    private final OperationStorage operationStorage;
    private final OperationTypeRepository operationTypeRepository;
    private final OperationStatusRepository operationStatusRepository;
    private final MerchantStorage merchantStorage;
    private final DtoMapper dtoMapper;

    public void create(UpsertOperationRequest request) {
        try {
            log.info("Creating operation");
            merchantStorage.isActive(request.merchantId(), MerchantStatus.ACTIVE);
            createOperationEntity(request);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (EntityNotActiveException exception) {
            throw new ForbiddenException(exception.getMessage());
        }
    }

    public GetOperationResponse getById(Long id) {
        try {
            var operation = getOperationEntityById(id);
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

    public void processPayment(PayRequest request) {
        try {
            var operation = operationStorage.findById(request.operationId());
            verifySum(operation, request);
            changeOperationStatus(operation, OperationStatus.PAID);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (EntityNotVerifiedException | EntityInvalidStatus exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public void cancelOperation(Long id) {
        try {
            var operation = operationStorage.findById(id);
            changeOperationStatus(operation, OperationStatus.FAILED);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (EntityInvalidStatus exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public void update(Long id, UpsertOperationRequest request) {
        try {
            log.info("Updating operation with id = {}", id);
            updateOperationEntity(id, request);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
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
        var operation = getOperationEntityById(id);
        var updated = operation.toBuilder()
                .status(status)
                .type(type)
                .parentId(request.parentId())
                .processedAt(request.processedAt())
                .build();
        operationStorage.save(updated);
    }

    private OperationTypeEntity getOperationType(Long id) {
        return operationTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation type with id = %d not found".formatted(id)));
    }

    private OperationStatusEntity getOperationStatus(Long id) {
        return operationStatusRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation status with id = %d not found".formatted(id)));
    }

    private OperationEntity getOperationEntityById(Long id) {
        log.info("Trying to find operation with id = {}", id);
        return operationStorage.findById(id);
    }

    private void verifySum(OperationEntity operation, PayRequest request) {
        if (!Objects.equals(operation.getSum(), request.sum())) {
            throw new EntityNotVerifiedException(
                    "Payment has invalid sum = %s, must be sum = %s"
                            .formatted(request.sum(), operation.getSum()));
        }
    }

    private void changeOperationStatus(OperationEntity operation, OperationStatus status) {
        if (Objects.equals(operation.getStatus().getCode(), OperationStatus.NEW)) {
            processOperation(operation, status);
        } else {
            throw new EntityInvalidStatus("Operation with id = %d is not NEW"
                    .formatted(operation.getId()));
        }
    }

    private void processOperation(OperationEntity operation, OperationStatus status) {
        var statusEntity = getOperationStatus(status);
        var updatedOperation = operation.toBuilder()
                .status(statusEntity)
                .processedAt(LocalDateTime.now())
                .build();
        operationStorage.save(updatedOperation);
    }

    private OperationStatusEntity getOperationStatus(OperationStatus status) {
        return operationStatusRepository.findByCode(status).orElseThrow(
                () -> new EntityNotFoundException("Operation status with code = %s not found".formatted(status.name())));
    }

}
