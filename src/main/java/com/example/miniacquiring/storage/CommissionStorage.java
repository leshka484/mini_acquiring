package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.repository.CommissionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CommissionStorage extends BaseStorage<CommissionEntity, Long, CommissionRepository> {

    public CommissionStorage(CommissionRepository commissionRepository) {
        super(commissionRepository);
    }

    public Long countMerchantCommissions(Long merchantId) {
        return repository.countMerchantCommissions(merchantId);
    }

    public BigDecimal sumMerchantCommissions(Long merchantId) {
        return repository.sumMerchantCommissions(merchantId);
    }

    public Long countMerchantCommissionsBetween(Long merchantId, LocalDateTime from, LocalDateTime to) {
        return repository.countMerchantCommissionsBetween(merchantId, from, to);
    }

    public BigDecimal sumMerchantCommissionsBetween(Long merchantId, LocalDateTime from, LocalDateTime to) {
        return repository.sumMerchantCommissionsBetween(merchantId, from, to);
    }

}
