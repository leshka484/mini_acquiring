package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.repository.CommissionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommissionStorage {

    private final CommissionRepository commissionRepository;

    public Long countMerchantCommissions(Long merchantId) {
        return commissionRepository.countMerchantCommissions(merchantId);
    }

    public BigDecimal sumMerchantCommissions(Long merchantId) {
        return commissionRepository.sumMerchantCommissions(merchantId);
    }

    public Long countMerchantCommissionsBetween(Long merchantId, LocalDateTime from, LocalDateTime to) {
        return commissionRepository.countMerchantCommissionsBetween(merchantId, from, to);
    }

    public BigDecimal sumMerchantCommissionsBetween(Long merchantId, LocalDateTime from, LocalDateTime to) {
        return commissionRepository.sumMerchantCommissionsBetween(merchantId, from, to);
    }

    public Long save(CommissionEntity commission) {
        commissionRepository.save(commission);
        return commission.getId();
    }

}
