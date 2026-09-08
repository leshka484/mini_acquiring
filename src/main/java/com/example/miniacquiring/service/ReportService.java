package com.example.miniacquiring.service;

import com.example.miniacquiring.core.dto.MerchantReport;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.repository.CommissionRepository;
import com.example.miniacquiring.storage.repository.OperationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportService {

    private final MerchantStorage merchantStorage;
    private final OperationRepository operationRepository;
    private final CommissionRepository commissionRepository;

    public MerchantReport getMerchantFullReport(Long merchantId) {
        var merchant = merchantStorage.getById(merchantId);
        Long operationsCount = operationRepository.countMerchantOperations(merchantId);
        Long commissionsCount = commissionRepository.countMerchantCommissions(merchantId);
        BigDecimal sumOperations = operationRepository.sumMerchantOperations(merchantId);
        BigDecimal sumCommissions = commissionRepository.sumMerchantCommissions(merchantId);
        return buildReport(merchant, operationsCount, sumOperations, commissionsCount, sumCommissions);
    }

    public MerchantReport getMerchantReportByTime(Long merchantId, LocalDateTime from, LocalDateTime to) {
        var merchant = merchantStorage.getById(merchantId);
        Long operationsCount = operationRepository.countMerchantOperationsBetween(merchantId, from, to);
        Long commissionsCount = commissionRepository.countMerchantCommissionsBetween(merchantId, from, to);
        BigDecimal sumOperations = operationRepository.sumMerchantOperationsBetween(merchantId, from, to);
        BigDecimal sumCommissions = commissionRepository.sumMerchantCommissionsBetween(merchantId, from, to);
        return buildReport(merchant, operationsCount, sumOperations, commissionsCount, sumCommissions);
    }

    private MerchantReport buildReport(
            MerchantEntity merchant,
            Long operationsCount,
            BigDecimal sumOperations,
            Long commissionsCount,
            BigDecimal sumCommissions) {
        return MerchantReport
                .builder()
                .merchantId(merchant.getId())
                .merchantName(merchant.getName())
                .operationsCount(operationsCount)
                .sumOperations(sumOperations)
                .commissionsCount(commissionsCount)
                .sumCommissions(sumCommissions)
                .build();
    }
}
