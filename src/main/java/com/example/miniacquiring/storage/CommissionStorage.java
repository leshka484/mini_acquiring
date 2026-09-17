package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.repository.CommissionRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommissionStorage {

    private final CommissionRepository commissionRepository;

    public CommissionEntity getById(Long id) {
        return commissionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Commission with id = %d not found".formatted(id))
        );
    }

    public void deleteById(List<Long> ids) {
        commissionRepository.deleteAllById(ids);
    }

    public void deleteById(Long id) {
        commissionRepository.deleteById(id);
    }

    public Long countMerchantCommissions(Long merchantId) {
        return commissionRepository.countMerchantCommissions(merchantId);
    }

    public BigDecimal sumMerchantCommissions(Long merchantId) {
        return commissionRepository.sumMerchantCommissions(merchantId);
    }

    public Long countMerchantCommissionsBetween(Long merchantId, CreateMerchantReportRequest request) {
        return commissionRepository.countMerchantCommissionsBetween(merchantId, request.startDateTime(), request.endDateTime());
    }

    public BigDecimal sumMerchantCommissionsBetween(Long merchantId, CreateMerchantReportRequest request) {
        return commissionRepository.sumMerchantCommissionsBetween(merchantId, request.startDateTime(), request.endDateTime());
    }

    public Long save(CommissionEntity commission) {
        commissionRepository.save(commission);
        return commission.getId();
    }

    public void saveAll(List<CommissionEntity> commissions) {
        commissionRepository.saveAll(commissions);
    }

}