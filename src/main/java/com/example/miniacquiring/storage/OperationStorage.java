package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.repository.OperationRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OperationStorage {

    private final OperationRepository operationRepository;

    public OperationEntity findById(Long id) {
        return operationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Merchant with id = %d does not exist".formatted(id))
        );
    }

    public List<OperationEntity> findPaid(String status) {
        var operations = operationRepository.findByStatus(status);
        if (operations.isEmpty()) {
            throw new EntityNotFoundException("No paid operations found");
        }
        return operations;
    }

    public Page<OperationEntity> findAll(Pageable pageable) {
        return operationRepository.findAll(pageable);
    }

    public void deleteById(List<Long> ids) {
        operationRepository.deleteAllById(ids);
    }

    public void deleteById(Long id) {
        operationRepository.deleteById(id);
    }

    public Long countMerchantOperations(Long merchantId) {
        return operationRepository.countMerchantOperations(merchantId);
    }

    public BigDecimal sumMerchantOperations(Long id) {
        return operationRepository.sumMerchantOperations(id);
    }

    public Long countMerchantOperationsBetween(Long merchantId, CreateMerchantReportRequest request) {
        return operationRepository.countMerchantOperationsBetween(merchantId, request.startDateTime(), request.endDateTime());
    }

    public BigDecimal sumMerchantOperationsBetween(Long id, CreateMerchantReportRequest request) {
        return operationRepository.sumMerchantOperationsBetween(id, request.startDateTime(), request.endDateTime());
    }

    public Long save(OperationEntity operation) {
        operationRepository.save(operation);
        return operation.getId();
    }

}
