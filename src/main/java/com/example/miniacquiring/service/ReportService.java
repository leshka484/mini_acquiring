package com.example.miniacquiring.service;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.dto.GetMerchantReportResponse;
import com.example.miniacquiring.core.enums.OperationStatus;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.service.utils.MoneyUtils;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.MerchantStorage;
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
    private final MerchantStorage merchantStorage;

    public GetMerchantReportResponse getMerchantFullReport(Long merchantId) {
        try {
            log.info("Getting full report by merchant id = {}", merchantId);
            return buildFullReport(merchantId);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }

    }

    public GetMerchantReportResponse getMerchantReportByTime(Long merchantId, CreateMerchantReportRequest request) {
        try {
            log.info("Getting report from {} to {} by merchant id = {}", request.startDateTime(), request.endDateTime(), merchantId);
            return buildTimeReport(merchantId, request);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    private GetMerchantReportResponse buildFullReport(Long id) {
        var merchant = merchantStorage.findById(id);
        var operationsCount = operationStorage
                .countMerchantOperations(id, OperationStatus.COMPLETED);
        var commissionsCount = commissionStorage.countMerchantCommissions(id);
        var sumOperations = operationStorage
                .sumMerchantOperations(id, OperationStatus.COMPLETED);
        var sumCommissions = commissionStorage.sumMerchantCommissions(id);
        return GetMerchantReportResponse
                .builder()
                .merchantId(merchant.getId())
                .merchantName(merchant.getName())
                .operationsCount(operationsCount)
                .sumOperations(MoneyUtils.convert(sumOperations))
                .commissionsCount(commissionsCount)
                .sumCommissions(MoneyUtils.convert(sumCommissions))
                .build();
    }

    private GetMerchantReportResponse buildTimeReport(Long id, CreateMerchantReportRequest request) {
        var merchant = merchantStorage.findById(id);
        var operationsCount = operationStorage
                .countMerchantOperationsBetween(id, OperationStatus.COMPLETED, request);
        var commissionsCount = commissionStorage.countMerchantCommissionsBetween(id, request);
        var sumOperations = operationStorage
                .sumMerchantOperationsBetween(id, OperationStatus.COMPLETED, request);
        var sumCommissions = commissionStorage.sumMerchantCommissionsBetween(id, request);
        return GetMerchantReportResponse
                .builder()
                .merchantId(merchant.getId())
                .merchantName(merchant.getName())
                .operationsCount(operationsCount)
                .sumOperations(MoneyUtils.convert(sumOperations))
                .commissionsCount(commissionsCount)
                .sumCommissions(MoneyUtils.convert(sumCommissions))
                .build();
    }

}
