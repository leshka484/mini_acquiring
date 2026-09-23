package com.example.miniacquiring.service;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.dto.GetMerchantReportResponse;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.MerchantEntity;
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
    private final MerchantStorage merchantStorage;

    public GetMerchantReportResponse getMerchantFullReport(Long merchantId) {
        log.info("Getting full report by merchant id = {}", merchantId);
        return buildFullReport(merchantId);
    }

    public GetMerchantReportResponse getMerchantReportByTime(Long merchantId, CreateMerchantReportRequest request) {
        log.info("Getting report from {} to {} by merchant id = {}", request.startDateTime(), request.endDateTime(), merchantId);
        return buildTimeReport(merchantId, request);
    }

    private GetMerchantReportResponse buildFullReport(Long id) {
        var merchant = getMerchant(id);
        Long operationsCount = operationStorage.countMerchantOperations(id);
        Long commissionsCount = commissionStorage.countMerchantCommissions(id);
        BigDecimal sumOperations = operationStorage.sumMerchantOperations(id);
        BigDecimal sumCommissions = commissionStorage.sumMerchantCommissions(id);
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

    private GetMerchantReportResponse buildTimeReport(Long id, CreateMerchantReportRequest request) {
        var merchant = getMerchant(id);
        Long operationsCount = operationStorage.countMerchantOperationsBetween(id, request);
        Long commissionsCount = commissionStorage.countMerchantCommissionsBetween(id, request);
        BigDecimal sumOperations = operationStorage.sumMerchantOperationsBetween(id, request);
        BigDecimal sumCommissions = commissionStorage.sumMerchantCommissionsBetween(id, request);
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

    private MerchantEntity getMerchant(Long id) {
        try {
            return merchantStorage.findById(id);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
