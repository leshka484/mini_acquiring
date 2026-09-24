package com.example.miniacquiring;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetCommissionResponse;
import com.example.miniacquiring.core.enums.CommissionType;
import com.example.miniacquiring.core.enums.OperationStatus;
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
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
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
import static org.mockito.ArgumentMatchers.any;
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

    @Captor
    private ArgumentCaptor<List<OperationEntity>> operationsCaptor;

    @Test
    void processCommissions_shouldCalculateWithPercentageStrategy() {
        var operation = createOperationWithPercentageCommission();
        var completedStatus = createOperationStatusEntity(OperationStatus.COMPLETED);
        when(operationStorage.findByStatus(OperationStatus.PAID))
                .thenReturn(List.of(operation));
        when(percentageCommissionStrategy.calculate(
                operation.getSum(),
                operation.getMerchant().getCommissionValue()))
                .thenReturn(new BigDecimal("25.00"));
        when(operationStorage.findOperationStatus(OperationStatus.COMPLETED))
                .thenReturn(completedStatus);
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
        verify(operationStorage).findOperationStatus(OperationStatus.COMPLETED);
        verify(operationStorage).saveAll(operationsCaptor.capture());
        var updatedOperations = operationsCaptor.getValue();
        assertThat(updatedOperations).hasSize(1);
        assertThat(updatedOperations.getFirst().getStatus())
                .isSameAs(completedStatus);
        verifyNoInteractions(fixedCommissionStrategy);
    }

    @Test
    void processCommissions_shouldCalculateWithFixedStrategy() {
        var operation = createOperationWithFixedCommission();
        var completedStatus = createOperationStatusEntity(OperationStatus.COMPLETED);
        when(operationStorage.findByStatus(OperationStatus.PAID)).thenReturn(List.of(operation));
        when(fixedCommissionStrategy.calculate(
                operation.getSum(),
                operation.getMerchant().getCommissionValue()))
                .thenReturn(new BigDecimal("30.00"));
        when(operationStorage.findOperationStatus(OperationStatus.COMPLETED))
                .thenReturn(completedStatus);
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
        verify(operationStorage).findOperationStatus(OperationStatus.COMPLETED);
        verify(operationStorage).saveAll(operationsCaptor.capture());
        var updatedOperations = operationsCaptor.getValue();
        assertThat(updatedOperations).hasSize(1);
        assertThat(updatedOperations.getFirst().getStatus()).isSameAs(completedStatus);
        verifyNoInteractions(percentageCommissionStrategy);
    }

    @Test
    void processCommissions_ShouldDoNothing_whenNoPaidOperations() {
        when(operationStorage.findByStatus(OperationStatus.PAID)).thenReturn(List.of());
        commissionService.processCommissions();
        verify(operationStorage).findByStatus(OperationStatus.PAID);
        verifyNoInteractions(commissionStorage);
        verify(operationStorage, never()).findOperationStatus(any());
        verify(operationStorage, never()).saveAll(any());
    }

    @Test
    void getById_shouldReturnCommissionResponse() {
        var commission = createCommissionEntity();
        var commissionResponse = createCommissionResponse();
        when(commissionStorage.getById(commission.getId()))
                .thenReturn(commission);
        when(dtoMapper.toResponse(commission)).thenReturn(commissionResponse);
        var actualResponse = commissionService.getById(commission.getId());
        assertThat(actualResponse).isSameAs(commissionResponse);
        verify(commissionStorage).getById(commission.getId());
        verify(dtoMapper).toResponse(commission);
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

    private OperationEntity createOperationWithPercentageCommission() {
        var commissionType = new CommissionTypeEntity(
                1L,
                CommissionType.PERCENTAGE,
                "Percentage");

        var merchant = MerchantEntity.builder()
                .commissionType(commissionType)
                .commissionValue(new BigDecimal("2.5"))
                .build();

        return OperationEntity.builder()
                .sum(new BigDecimal("1000.00"))
                .merchant(merchant)
                .build();
    }

    private OperationEntity createOperationWithFixedCommission() {
        var commissionType = new CommissionTypeEntity(
                1L,
                CommissionType.FIXED,
                "Fixed");

        var merchant = MerchantEntity.builder()
                .commissionType(commissionType)
                .commissionValue(new BigDecimal("30"))
                .build();

        return OperationEntity.builder()
                .sum(new BigDecimal("1000.00"))
                .merchant(merchant)
                .build();
    }

    private CommissionEntity createCommissionEntity() {
        var operation = createOperationWithPercentageCommission();
        var id = 1L;
        return CommissionEntity
                .builder()
                .id(id)
                .operation(operation)
                .totalCommission(new BigDecimal("25.00"))
                .processedAt(LocalDateTime.of(2026, 1, 1, 0, 0))
                .build();
    }

    private GetCommissionResponse createCommissionResponse() {
        return new GetCommissionResponse(
                1L,
                1L,
                new BigDecimal("25.00"),
                LocalDateTime.of(2026, 1, 1, 0, 0));
    }

    private OperationStatusEntity createOperationStatusEntity(OperationStatus status) {
        return new OperationStatusEntity(
                1L,
                status,
                "Completed");
    }

}
