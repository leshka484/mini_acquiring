package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.repository.CommissionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommissionStorage {

    private final CommissionRepository commissionRepository;

    public void deleteById(List<Long> ids, String errorText) {
        List<Long> uniqueIds = ids.stream()
                .distinct()
                .toList();
        if (commissionRepository.existsAllById(uniqueIds)) { //TODO: перед удалением можно не проверять на существование а просто в сервисе исключение отлавливать
            throw new IllegalArgumentException(errorText);
        }
        commissionRepository.deleteAllById(uniqueIds);
    }

    public void deleteById(Long id, String errorText) {
        if (commissionRepository.existsById(id)) {
            throw new IllegalArgumentException(errorText);
        }
        commissionRepository.deleteById(id);
    }

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

    public Long save(CommissionEntity commission) { //TODO: dead code
        commissionRepository.save(commission);
        return commission.getId();
    }

}
