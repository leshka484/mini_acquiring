package com.example.miniacquiring.service;

import com.example.miniacquiring.core.dto.operation.CreateOperationRequest;
import com.example.miniacquiring.core.dto.operation.GetOperationResponse;
import com.example.miniacquiring.core.dto.operation.UpdateOperationRequest;
import com.example.miniacquiring.core.mapper.EntityMapper;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStatusStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.OperationTypeStorage;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    private final OperationTypeStorage operationTypeStorage;
    private final OperationStatusStorage operationStatusStorage;
    private final MerchantStorage merchantStorage;
    private final EntityMapper entityMapper;

    public GetOperationResponse create(CreateOperationRequest request) { //TODO: почитай про SOLID, а конкретно про Single Responsibility
        var type = operationTypeStorage.findById(request.typeId()); //TODO: проверяй на наличие активного мерчанта(отдельный метод валидации)
        var status = operationStatusStorage.findById(request.statusId());
        var merchant = merchantStorage.findById(request.merchantId());
        var operation = buildOperationEntity(merchant,
                status,
                request.sum(),
                type,
                request.parentId(),
                request.createdAt(),
                request.processedAt());

        operationStorage.save(operation);
        log.info("Operation id = {} created", operation.getId());
        return entityMapper.toResponse(operation); //TODO: сделай все Create и update и delete void
    }

    public GetOperationResponse getById(Long id) {
        var operation = operationStorage.findById(id);
        log.info("Operation with id = {} found", id);
        return entityMapper.toResponse(operation);
    }

    public Page<GetOperationResponse> getAll(Pageable pageable) {
        var operations = operationStorage.findAll(pageable);
        log.info("Getting all operations");
        return operations.map(entityMapper::toResponse);
    }

    public GetOperationResponse update(Long id, UpdateOperationRequest request) {
        operationStorage.findById(id); //TODO: лупани проверку
        var type = operationTypeStorage.findById(request.typeId());
        var status = operationStatusStorage.findById(request.statusId());
        var operation = buildOperationEntity(id,
                status,
                type,
                request.parentId(),
                request.processedAt());
        operationStorage.save(operation);
        log.info("Operation with id = {}", id);
        return entityMapper.toResponse(operation);
    }

    public void deleteById(List<Long> ids) {
        operationStorage.deleteById(ids);
        log.info("Merchants deleted");
    }

    public void deleteById(Long id) {
        operationStorage.deleteById(id);
    }

    private OperationEntity buildOperationEntity(Long id, OperationStatusEntity status, OperationTypeEntity type,
                                                 Long parentId, LocalDateTime processedAt) {
        return OperationEntity
                .builder()
                .id(id)
                .status(status)
                .type(type)
                .parentId(parentId)
                .processedAt(processedAt)
                .build();
    }

    private OperationEntity buildOperationEntity(MerchantEntity merchant, OperationStatusEntity status, BigDecimal sum,
                                                 OperationTypeEntity type,
                                                 Long parentId,
                                                 LocalDateTime createdAt,
                                                 LocalDateTime processedAt) { //TODO: Старайся не передавать в методы более трёх параметров
        return OperationEntity
                .builder()
                .merchant(merchant)
                .status(status)
                .sum(sum)
                .type(type)
                .parentId(parentId)
                .createdAt(createdAt)
                .processedAt(processedAt)
                .build();
    }

}
