package com.example.miniacquiring;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.enums.CommissionType;
import com.example.miniacquiring.core.enums.MerchantStatus;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.service.ReportService;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private OperationStorage operationStorage;

    @Mock
    private CommissionStorage commissionStorage;

    @Mock
    private MerchantStorage merchantStorage;

    @InjectMocks
    private ReportService reportService;

    @Test
    void getMerchantFullReport_shouldReturnReport() {
        var merchant = createMerchantEntity();
        var merchantId = merchant.getId();
        when(merchantStorage.findById(merchantId)).thenReturn(merchant);
        when(operationStorage.countMerchantOperations(merchantId))
                .thenReturn(10L);
        when(commissionStorage.countMerchantCommissions(merchantId))
                .thenReturn(5L);
        when(operationStorage.sumMerchantOperations(merchantId))
                .thenReturn(new BigDecimal("1000.00"));
        when(commissionStorage.sumMerchantCommissions(merchantId))
                .thenReturn(new BigDecimal("50.00"));
        var result = reportService.getMerchantFullReport(merchantId);
        verify(merchantStorage).findById(merchantId);
        verify(operationStorage).countMerchantOperations(merchantId);
        verify(operationStorage).sumMerchantOperations(merchantId);
        verify(commissionStorage).countMerchantCommissions(merchantId);
        verify(commissionStorage).sumMerchantCommissions(merchantId);
        assertThat(result.merchantId()).isEqualTo(merchantId);
        assertThat(result.merchantName()).isEqualTo(merchant.getName());
        assertThat(result.operationsCount()).isEqualTo(10L);
        assertThat(result.sumOperations()).isEqualByComparingTo("1000.00");
        assertThat(result.commissionsCount()).isEqualTo(5L);
        assertThat(result.sumCommissions()).isEqualByComparingTo("50.00");
    }

    @Test
    void getMerchantFullReport_shouldThrowNotFoundException_whenMerchantNotFound() {
        var merchantId = 100L;
        var message = "Merchant with id = %d not found".formatted(merchantId);
        when(merchantStorage.findById(merchantId))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(() -> reportService.getMerchantFullReport(merchantId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).findById(merchantId);
        verifyNoInteractions(operationStorage);
        verifyNoInteractions(commissionStorage);
    }

    @Test
    void getMerchantReportByTime_shouldReturnReport() {
        var merchant = createMerchantEntity();
        var merchantId = merchant.getId();
        var request = createMerchantReportRequest();
        when(merchantStorage.findById(merchantId)).thenReturn(merchant);
        when(operationStorage.countMerchantOperationsBetween(merchantId, request))
                .thenReturn(10L);
        when(commissionStorage.countMerchantCommissionsBetween(merchantId, request))
                .thenReturn(10L);
        when(operationStorage.sumMerchantOperationsBetween(merchantId, request))
                .thenReturn(new BigDecimal("700.00"));
        when(commissionStorage.sumMerchantCommissionsBetween(merchantId, request))
                .thenReturn(new BigDecimal("35.00"));
        var result = reportService.getMerchantReportByTime(merchantId, request);
        verify(merchantStorage).findById(merchantId);
        verify(operationStorage)
                .countMerchantOperationsBetween(merchantId, request);
        verify(operationStorage)
                .sumMerchantOperationsBetween(merchantId, request);
        verify(commissionStorage)
                .countMerchantCommissionsBetween(merchantId, request);
        verify(commissionStorage)
                .sumMerchantCommissionsBetween(merchantId, request);
        assertThat(result.merchantId()).isEqualTo(merchantId);
        assertThat(result.merchantName()).isEqualTo(merchant.getName());
        assertThat(result.operationsCount()).isEqualTo(10L);
        assertThat(result.sumOperations()).isEqualByComparingTo("700.00");
        assertThat(result.commissionsCount()).isEqualTo(10L);
        assertThat(result.sumCommissions()).isEqualByComparingTo("35.00");
    }

    @Test
    void getMerchantReportByTime_shouldThrowNotFoundException_whenMerchantNotFound() {
        var merchantId = 100L;
        var request = createMerchantReportRequest();
        var message = "Merchant with id = %d not found".formatted(merchantId);
        when(merchantStorage.findById(merchantId))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(
                () -> reportService.getMerchantReportByTime(merchantId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).findById(merchantId);
        verifyNoInteractions(operationStorage);
        verifyNoInteractions(commissionStorage);
    }

    private MerchantEntity createMerchantEntity() {
        return MerchantEntity
                .builder()
                .id(1L)
                .name("test")
                .commissionValue(new BigDecimal("10.00"))
                .commissionType(createCommissionType())
                .status(createMerchantStatus())
                .build();
    }

    private CommissionTypeEntity createCommissionType() {
        return new CommissionTypeEntity(
                1L,
                CommissionType.PERCENTAGE,
                "Percentage");
    }

    private MerchantStatusEntity createMerchantStatus() {
        return new MerchantStatusEntity(
                1L,
                MerchantStatus.ACTIVE,
                "Active");
    }

    private CreateMerchantReportRequest createMerchantReportRequest() {
        return new CreateMerchantReportRequest(
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 31, 23, 59)
        );
    }

}
