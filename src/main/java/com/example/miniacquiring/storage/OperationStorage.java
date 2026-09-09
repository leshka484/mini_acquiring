package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.repository.OperationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperationStorage extends BaseStorage<OperationEntity, Long, OperationRepository> {

    public OperationStorage(OperationRepository operationRepository) {
        super(operationRepository);
    }

    public Long countMerchantOperations(Long merchantId) {
        return repository.countMerchantOperations(merchantId);
    }

    public BigDecimal sumMerchantOperations(Long id) {
        return repository.sumMerchantOperations(id);
    }

    public Long countMerchantOperationsBetween(Long merchantId, LocalDateTime from, LocalDateTime to) {
        return repository.countMerchantOperationsBetween(merchantId, from, to);
    }

    public BigDecimal sumMerchantOperationsBetween(Long id, LocalDateTime from, LocalDateTime to) {
        return repository.sumMerchantOperationsBetween(id, from, to);
    }

}
