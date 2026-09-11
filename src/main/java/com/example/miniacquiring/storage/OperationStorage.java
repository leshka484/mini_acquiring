package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.repository.OperationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OperationStorage {

    private final OperationRepository operationRepository;

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
