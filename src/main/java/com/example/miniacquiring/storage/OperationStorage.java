package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.enums.OperationStatus;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.repository.OperationRepository;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OperationStorage {

    private final OperationRepository operationRepository;
    private final OperationStatusRepository operationStatusRepository;

    public OperationEntity findById(Long id) {
        return operationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation with id = %d does not exist".formatted(id))
        );
    }

    public List<OperationEntity> findByStatus(OperationStatus status) {
        return operationRepository.findByStatus(status);
    }

    public Page<OperationEntity> findAll(Pageable pageable) {
        return operationRepository.findAll(pageable);
    }

    public Long findIdByPublicId(UUID publicId) {
        var operation = operationRepository.findByPublicId(publicId).orElseThrow(
                () -> new EntityNotFoundException("Operation with public id = %s does not exist".formatted(publicId)));
        return operation.getId();
    }

    public Long countMerchantOperations(Long merchantId, OperationStatus status) {
        return operationRepository.countMerchantOperations(merchantId, status);
    }

    public Long sumMerchantOperations(Long id, OperationStatus status) {
        return operationRepository.sumMerchantOperations(id, status);
    }

    public Long countMerchantOperationsBetween(Long merchantId, OperationStatus status, CreateMerchantReportRequest request) {
        return operationRepository
                .countMerchantOperationsBetween(merchantId, status, request.startDateTime(), request.endDateTime());
    }

    public Long sumMerchantOperationsBetween(Long id, OperationStatus status, CreateMerchantReportRequest request) {
        return operationRepository
                .sumMerchantOperationsBetween(id, status, request.startDateTime(), request.endDateTime());
    }

    public Long save(OperationEntity operation) {
        operationRepository.save(operation);
        return operation.getId();
    }

    public void saveAll(List<OperationEntity> commissions) {
        operationRepository.saveAll(commissions);
    }

    public OperationStatusEntity findOperationStatus(OperationStatus status) {
        return operationStatusRepository.findByCode(status).orElseThrow(
                () -> new EntityNotFoundException("Operation status with code = %s not found".formatted(status))
        );
    }

}
