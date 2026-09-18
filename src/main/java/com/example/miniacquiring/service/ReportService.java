package com.example.miniacquiring.service;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.dto.GetMerchantReportResponse;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.OperationStorage;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportService {

    private final OperationStorage operationStorage;
    private final CommissionStorage commissionStorage;
    private final MerchantService merchantService;

    public GetMerchantReportResponse getMerchantFullReport(Long merchantId) {
        log.info("Getting full report by merchant id = {}", merchantId);
        return buildFullReport(merchantId);
    }

    public GetMerchantReportResponse getMerchantReportByTime(Long merchantId, CreateMerchantReportRequest request) {
        log.info("Getting report from {} to {} by merchant id = {}", request.startDateTime(), request.endDateTime(), merchantId);
        return buildTimeReport(merchantId, request);
    }

    private GetMerchantReportResponse buildFullReport(Long merchantId) {
        var merchant = merchantService.getById(merchantId);
        Long operationsCount = operationStorage.countMerchantOperations(merchantId);
        Long commissionsCount = commissionStorage.countMerchantCommissions(merchantId);
        BigDecimal sumOperations = operationStorage.sumMerchantOperations(merchantId);
        BigDecimal sumCommissions = commissionStorage.sumMerchantCommissions(merchantId);
        return GetMerchantReportResponse
                .builder()
                .merchantId(merchant.getId())
                .merchantName(merchant.getName())
                .operationsCount(operationsCount)
                .sumOperations(sumOperations)
                .commissionsCount(commissionsCount)
                .sumCommissions(sumCommissions)
                .build();
    }

    private GetMerchantReportResponse buildTimeReport(Long merchantId, CreateMerchantReportRequest request) {
        var merchant = merchantService.getById(merchantId);
        Long operationsCount = operationStorage.countMerchantOperationsBetween(merchantId, request);
        Long commissionsCount = commissionStorage.countMerchantCommissionsBetween(merchantId, request);
        BigDecimal sumOperations = operationStorage.sumMerchantOperationsBetween(merchantId, request);
        BigDecimal sumCommissions = commissionStorage.sumMerchantCommissionsBetween(merchantId, request);
        return GetMerchantReportResponse
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
