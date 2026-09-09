package com.example.miniacquiring.service;

import com.example.miniacquiring.core.dto.reports.MerchantReport;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportService {

    private final MerchantStorage merchantStorage;
    private final OperationStorage operationStorage;
    private final CommissionStorage commissionStorage;

    public MerchantReport getMerchantFullReport(Long merchantId) {
        var merchant = merchantStorage.getById(merchantId);
        Long operationsCount = operationStorage.countMerchantOperations(merchantId);
        Long commissionsCount = commissionStorage.countMerchantCommissions(merchantId);
        BigDecimal sumOperations = operationStorage.sumMerchantOperations(merchantId);
        BigDecimal sumCommissions = commissionStorage.sumMerchantCommissions(merchantId);
        return buildMerchantReport(merchant, operationsCount, sumOperations, commissionsCount, sumCommissions);
    }

    public MerchantReport getMerchantReportByTime(Long merchantId, LocalDateTime from, LocalDateTime to) {
        var merchant = merchantStorage.getById(merchantId);
        Long operationsCount = operationStorage.countMerchantOperationsBetween(merchantId, from, to);
        Long commissionsCount = commissionStorage.countMerchantCommissionsBetween(merchantId, from, to);
        BigDecimal sumOperations = operationStorage.sumMerchantOperationsBetween(merchantId, from, to);
        BigDecimal sumCommissions = commissionStorage.sumMerchantCommissionsBetween(merchantId, from, to);
        return buildMerchantReport(merchant, operationsCount, sumOperations, commissionsCount, sumCommissions);
    }

    private MerchantReport buildMerchantReport(
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
