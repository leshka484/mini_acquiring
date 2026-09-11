package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.repository.OperationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                () -> new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id))
        );
    }

    public Page<OperationEntity> findAll(Pageable pageable) {
        return operationRepository.findAll(pageable);
    }

    public void deleteById(List<Long> ids) {
        List<Long> uniqueIds = ids.stream()
                .distinct()
                .toList();
        if (operationRepository.existsAllById(uniqueIds)) {
            throw new IllegalArgumentException("Some operations do not exist");
        }
        operationRepository.deleteAllById(uniqueIds);
    }

    public void deleteById(Long id) {
        if (operationRepository.existsById(id)) {
            throw new IllegalArgumentException("Operation with id = %d does not exist".formatted(id));
        }
        operationRepository.deleteById(id);
    }

    public Long countMerchantOperations(Long merchantId) {
        return operationRepository.countMerchantOperations(merchantId);
    }

    public BigDecimal sumMerchantOperations(Long id) {
        return operationRepository.sumMerchantOperations(id);
    }

    public Long countMerchantOperationsBetween(Long merchantId, LocalDateTime from, LocalDateTime to) {
        return operationRepository.countMerchantOperationsBetween(merchantId, from, to);
    }

    public BigDecimal sumMerchantOperationsBetween(Long id, LocalDateTime from, LocalDateTime to) {
        return operationRepository.sumMerchantOperationsBetween(id, from, to);
    }

    public Long save(OperationEntity operation) {
        operationRepository.save(operation);
        return operation.getId();
    }

}
