package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.repository.CommissionRepository;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommissionStorage {

    private final CommissionRepository commissionRepository;
    private final CommissionTypeRepository commissionTypeRepository;

    public CommissionEntity findById(Long id) {
        return commissionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Commission with id = %d not found".formatted(id))
        );
    }

    public Page<CommissionEntity> findAll(Pageable pageable) {
        return commissionRepository.findAll(pageable);
    }

    public Long countMerchantCommissions(Long merchantId) {
        return commissionRepository.countMerchantCommissions(merchantId);
    }

    public Long sumMerchantCommissions(Long merchantId) {
        return commissionRepository.sumMerchantCommissions(merchantId);
    }

    public Long countMerchantCommissionsBetween(Long merchantId, CreateMerchantReportRequest request) {
        return commissionRepository.countMerchantCommissionsBetween(merchantId, request.startDateTime(), request.endDateTime());
    }

    public Long sumMerchantCommissionsBetween(Long merchantId, CreateMerchantReportRequest request) {
        return commissionRepository.sumMerchantCommissionsBetween(merchantId, request.startDateTime(), request.endDateTime());
    }

    public void saveAll(List<CommissionEntity> commissions) {
        commissionRepository.saveAll(commissions);
    }

    @Cacheable(cacheNames = "commissionTypes", key = "#id")
    public CommissionTypeEntity getCommissionType(Long id) {
        return commissionTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Commission type with id = %d not found".formatted(id))
        );
    }

}