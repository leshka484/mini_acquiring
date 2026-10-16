package com.example.miniacquiring.service;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.CreateOperationRequest;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.ProcessOperationRequest;
import com.example.miniacquiring.core.enums.MerchantStatus;
import com.example.miniacquiring.core.enums.OperationStatus;
import com.example.miniacquiring.core.enums.OperationType;
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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
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
    private final MerchantStorage merchantStorage;
    private final DtoMapper dtoMapper;

    public OperationEntity create(CreateOperationRequest request) {
        try {
            log.info("Creating operation");
            merchantStorage.isActive(request.merchantId(), MerchantStatus.ACTIVE);
            return createOperationEntity(request);
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

    public void processPayment(ProcessOperationRequest request) {
        try {
            var operation = operationStorage.findById(request.operationId());
            verifySum(operation, request);
            payOperation(operation);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (EntityNotVerifiedException | EntityInvalidStatus exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public void processCancel(ProcessOperationRequest request) {
        try {
            var operation = operationStorage.findById(request.operationId());
            verifySum(operation, request);
            cancelOperation(operation);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (EntityNotVerifiedException | EntityInvalidStatus exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public Long getIdByPublicId(UUID publicId) {
        try {
            log.info("Getting operation id by it's public id = {}", publicId);
            return operationStorage.findIdByPublicId(publicId);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    private OperationEntity createOperationEntity(CreateOperationRequest request) {
        var type = operationStorage.findOperationType(request.typeId());
        var status = operationStorage.findOperationStatus(request.statusId());
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
        return operation;
    }

    private OperationEntity getOperationEntityById(Long id) {
        log.info("Trying to find operation with id = {}", id);
        return operationStorage.findById(id);
    }

    private void verifySum(OperationEntity operation, ProcessOperationRequest request) {
        if (!Objects.equals(operation.getSum(), request.sum())) {
            throw new EntityNotVerifiedException(
                    "Invalid sum: provided = %s, must be = %s"
                            .formatted(request.sum(), operation.getSum()));
        }
    }

    private void cancelOperation(OperationEntity operation) {
        if (isReturnOperation(operation)) {
            throw new EntityInvalidStatus("Cannot cancel operation with RETURN type");
        }
        var operationStatusCode = operation.getStatus().getCode();
        switch (operationStatusCode) {
            case NEW -> processOperation(operation, OperationStatus.REJECTED);
            case PAID, COMPLETED -> refundOperation(operation);
            case REJECTED, REFUNDED, FAILED -> throw new EntityInvalidStatus(
                    "Cannot cancel operation with %s status".formatted(operationStatusCode));
        }
    }

    private void payOperation(OperationEntity operation) {
        if (isReturnOperation(operation)) {
            throw new EntityInvalidStatus("Cannot pay operation with RETURN type");
        }
        var operationStatusCode = operation.getStatus().getCode();
        if (!Objects.equals(operationStatusCode, OperationStatus.NEW)) {
            throw new EntityInvalidStatus("Operation with id = %d is not new"
                    .formatted(operation.getId()));
        }
        processOperation(operation, OperationStatus.PAID);
    }

    private boolean isReturnOperation(OperationEntity operation) {
        return operation.getType().getCode() == OperationType.RETURN;
    }

    private void refundOperation(OperationEntity operation) {
        processOperation(operation, OperationStatus.REFUNDED);
        createRefundOperation(operation);
    }

    private void processOperation(OperationEntity operation, OperationStatus status) {
        var statusEntity = operationStorage.findOperationStatus(status);
        var updatedOperation = operation.toBuilder()
                .status(statusEntity)
                .processedAt(LocalDateTime.now())
                .build();
        operationStorage.save(updatedOperation);
    }

    private void createRefundOperation(OperationEntity operation) {
        var refundOperation = OperationEntity.builder()
                .merchant(operation.getMerchant())
                .status(operationStorage.findOperationStatus(OperationStatus.NEW))
                .sum(operation.getSum())
                .type(operationStorage.findOperationType(OperationType.RETURN))
                .parentId(operation.getId())
                .createdAt(LocalDateTime.now())
                .build();
        operationStorage.save(refundOperation);
    }

}
