package com.example.miniacquiring;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.service.CommissionService;
import com.example.miniacquiring.service.commisstionStrategy.FixedCommissionStrategy;
import com.example.miniacquiring.service.commisstionStrategy.PercentageCommissionStrategy;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommissionServiceTest {

    @Mock
    private CommissionStorage commissionStorage;

    @Mock
    private OperationStorage operationStorage;

    @Mock
    private DtoMapper dtoMapper;

    @Mock
    private PercentageCommissionStrategy percentageCommissionStrategy;

    @Mock
    private FixedCommissionStrategy fixedCommissionStrategy;

    @InjectMocks
    private CommissionService commissionService;

    @Captor
    private ArgumentCaptor<List<CommissionEntity>> commissionsCaptor;

    @Test
    void processCommissions_ShouldDoNothing_whenNoPaidOperations() {
        when(operationStorage.findPaid()).thenReturn(List.of());
        commissionService.processCommissions();
        verify(operationStorage).findPaid();
        verifyNoInteractions(commissionStorage);
        verify(operationStorage, never()).completeAllPaid();
    }

    @Test
    void processCommissions_shouldCalculateWithPercentageStrategy() {
        var operation = getOperationWithPercentageCommission();
        when(operationStorage.findPaid()).thenReturn(List.of(operation));
        when(percentageCommissionStrategy.calculate(
                operation.getSum(),
                operation.getMerchant().getCommissionValue()))
                .thenReturn(new BigDecimal("25.00"));
        commissionService.processCommissions();
        verify(percentageCommissionStrategy).calculate(
                operation.getSum(),
                operation.getMerchant().getCommissionValue());
        verify(commissionStorage).saveAll(commissionsCaptor.capture());
        var savedCommissions = commissionsCaptor.getValue();
        assertThat(savedCommissions).hasSize(1);
        var commission = savedCommissions.getFirst();
        assertThat(commission.getOperation()).isSameAs(operation);
        assertThat(commission.getTotalCommission()).isEqualByComparingTo("25.00");
        assertThat(commission.getProcessedAt()).isNotNull();
        verify(operationStorage).completeAllPaid();
        verifyNoInteractions(fixedCommissionStrategy);
    }

    @Test
    void processCommissions_shouldCalculateWithFixedStrategy() {
        var operation = getOperationWithFixedCommission();
        when(operationStorage.findPaid()).thenReturn(List.of(operation));
        when(fixedCommissionStrategy.calculate(
                operation.getSum(),
                operation.getMerchant().getCommissionValue()))
                .thenReturn(new BigDecimal("30.00"));
        commissionService.processCommissions();
        verify(fixedCommissionStrategy).calculate(
                operation.getSum(),
                operation.getMerchant().getCommissionValue());
        verify(commissionStorage).saveAll(commissionsCaptor.capture());
        var savedCommissions = commissionsCaptor.getValue();
        assertThat(savedCommissions).hasSize(1);
        var commission = savedCommissions.getFirst();
        assertThat(commission.getOperation()).isSameAs(operation);
        assertThat(commission.getTotalCommission()).isEqualByComparingTo("30.00");
        assertThat(commission.getProcessedAt()).isNotNull();
        verify(operationStorage).completeAllPaid();
        verifyNoInteractions(percentageCommissionStrategy);
    }

    @Test
    void processCommissions_shouldThrowException_whenCommissionTypeUnknown() {
        var operation = getOperationWithUnknownCommission();
        when(operationStorage.findPaid()).thenReturn(List.of(operation));
        assertThatThrownBy(
                () -> commissionService.processCommissions())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No strategy for commission type UNKNOWN");
        verify(commissionStorage, never()).saveAll(anyList());
        verify(operationStorage, never()).completeAllPaid();
    }

    @Test
    void getById_shouldReturnCommissionEntity() {
        var commission = getCommissionEntity();
        when(commissionStorage.getById(commission.getId()))
                .thenReturn(commission);
        var actualCommission = commissionService.getById(commission.getId());
        assertThat(actualCommission).isSameAs(commission);
        verify(commissionStorage).getById(commission.getId());
    }

    @Test
    void getById_shouldThrowException_whenNoCommissionWithThisId() {
        var id = 100L;
        var message = "Commission with id = %d not found".formatted(id);
        when(commissionStorage.getById(id))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(
                () -> commissionService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(commissionStorage).getById(id);
    }

    @Test
    void deleteById_shouldCallStorage() {
        var ids = List.of(1L, 2L, 3L);
        commissionService.deleteById(ids);
        verify(commissionStorage).deleteById(ids);
    }

    private OperationEntity getOperationWithPercentageCommission() {
        var commissionType = CommissionTypeEntity.builder()
                .type(Const.PERCENTAGE_COMMISSION)
                .build();

        var merchant = MerchantEntity.builder()
                .commissionType(commissionType)
                .commissionValue(new BigDecimal("2.5"))
                .build();

        return OperationEntity.builder()
                .sum(new BigDecimal("1000.00"))
                .merchant(merchant)
                .build();
    }

    private OperationEntity getOperationWithFixedCommission() {
        var commissionType = CommissionTypeEntity.builder()
                .type(Const.FIXED_COMMISSION)
                .build();

        var merchant = MerchantEntity.builder()
                .commissionType(commissionType)
                .commissionValue(new BigDecimal("30"))
                .build();

        return OperationEntity.builder()
                .sum(new BigDecimal("1000.00"))
                .merchant(merchant)
                .build();
    }

    private OperationEntity getOperationWithUnknownCommission() {
        var commissionType = CommissionTypeEntity.builder()
                .type("UNKNOWN")
                .build();

        var merchant = MerchantEntity.builder()
                .commissionType(commissionType)
                .commissionValue(new BigDecimal("30"))
                .build();

        return OperationEntity.builder()
                .sum(new BigDecimal("1000.00"))
                .merchant(merchant)
                .build();
    }

    private CommissionEntity getCommissionEntity() {
        var operation = getOperationWithPercentageCommission();
        var id = 1L;
        return CommissionEntity
                .builder()
                .id(id)
                .operation(operation)
                .totalCommission(new BigDecimal("25.00"))
                .processedAt(LocalDateTime.now())
                .build();
    }

}
