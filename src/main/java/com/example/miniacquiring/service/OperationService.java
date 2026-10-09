package com.example.miniacquiring.service;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.PayRequest;
import com.example.miniacquiring.core.dto.CreateOperationRequest;
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
import com.example.miniacquiring.core.utils.MoneyUtils;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;
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
    private final OperationTypeRepository operationTypeRepository;
    private final OperationStatusRepository operationStatusRepository;
    private final ReferenceDataService referenceDataService;
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
            changeOperationStatus(operation, OperationStatus.CANCELLED);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (EntityInvalidStatus exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public OperationStatusEntity getOperationStatus(OperationStatus status) {
        return operationStatusRepository.findByCode(status).orElseThrow(
                () -> new EntityNotFoundException("Operation status with code = %s not found".formatted(status.name())));
    }

    public OperationTypeEntity getOperationType(OperationType type) {
        return operationTypeRepository.findByCode(type).orElseThrow(
                () -> new EntityNotFoundException("Operation type with code = %s not found".formatted(type.name())));
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
        var type = referenceDataService.getOperationType(request.typeId());
        var status = referenceDataService.getOperationStatus(request.statusId());
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

    private void verifySum(OperationEntity operation, PayRequest request) {
        if (!Objects.equals(operation.getSum(), request.sum())) {
            throw new EntityNotVerifiedException(
                    "Payment has invalid sum = %s, sum must be = %s"
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

}
