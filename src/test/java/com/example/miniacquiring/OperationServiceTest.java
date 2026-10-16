package com.example.miniacquiring;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.CreateOperationRequest;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.ProcessOperationRequest;
import com.example.miniacquiring.core.enums.CommissionType;
import com.example.miniacquiring.core.enums.MerchantStatus;
import com.example.miniacquiring.core.enums.OperationStatus;
import com.example.miniacquiring.core.enums.OperationType;
import com.example.miniacquiring.core.exception.BadRequestException;
import com.example.miniacquiring.core.exception.EntityNotActiveException;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.ForbiddenException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.service.OperationService;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

    @Mock
    private OperationStorage operationStorage;

    @Mock
    private MerchantStorage merchantStorage;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private OperationService operationService;

    @Captor
    private ArgumentCaptor<OperationEntity> operationCaptor;

    @Test
    void create_shouldCreateNewOperation() {
        var request = createUpdateRequest();
        var operationType = createOperationTypeEntity(OperationType.PAYMENT);
        var operationStatus = createOperationStatusEntity(OperationStatus.NEW);
        var merchant = createMerchantEntity();
        when(operationStorage.findOperationType(request.typeId()))
                .thenReturn(operationType);
        when(operationStorage.findOperationStatus(request.statusId()))
                .thenReturn(operationStatus);
        when(merchantStorage.findById(request.merchantId()))
                .thenReturn(merchant);
        operationService.create(request);
        verify(operationStorage).findOperationType(request.typeId());
        verify(operationStorage).findOperationStatus(request.statusId());
        verify(merchantStorage).findById(request.merchantId());
        verify(merchantStorage)
                .isActive(request.merchantId(), MerchantStatus.ACTIVE);
        verify(operationStorage).save(operationCaptor.capture());
        var savedOperation = operationCaptor.getValue();
        assertThat(savedOperation.getMerchant()).isSameAs(merchant);
        assertThat(savedOperation.getStatus()).isSameAs(operationStatus);
        assertThat(savedOperation.getType()).isSameAs(operationType);
        assertThat(savedOperation.getSum()).isEqualByComparingTo(request.sum());
        assertThat(savedOperation.getParentId()).isEqualTo(request.parentId());
        assertThat(savedOperation.getCreatedAt()).isNotNull();
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenTypeNotFound() {
        var request = createUpdateRequest();
        var message = "Operation type with id = %d not found".formatted(request.typeId());
        when(operationStorage.findOperationType(request.typeId()))
                .thenThrow(new NotFoundException(message));
        assertThatThrownBy(() -> operationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).isActive(request.merchantId(), MerchantStatus.ACTIVE);
        verify(operationStorage).findOperationType(request.typeId());
        verify(merchantStorage, never()).findById(request.merchantId());
    }

    @Test
    void create_shouldThrowNotFoundException_whenStatusNotFound() {
        var request = createUpdateRequest();
        var type = createOperationTypeEntity(OperationType.PAYMENT);
        var message = "Operation status with id = %d not found".formatted(request.statusId());
        when(operationStorage.findOperationType(request.typeId()))
                .thenReturn(type);
        when(operationStorage.findOperationStatus(request.statusId()))
                .thenThrow(new NotFoundException(message));
        assertThatThrownBy(() -> operationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).isActive(request.merchantId(), MerchantStatus.ACTIVE);
        verify(operationStorage).findOperationType(request.typeId());
        verify(operationStorage).findOperationStatus(request.statusId());
        verify(merchantStorage, never()).findById(request.merchantId());
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenMerchantNotFound() {
        var request = createUpdateRequest();
        var type = createOperationTypeEntity(OperationType.PAYMENT);
        var status = createOperationStatusEntity(OperationStatus.NEW);
        var message = "Merchant with id = %d not found".formatted(request.merchantId());
        when(operationStorage.findOperationType(request.typeId()))
                .thenReturn(type);
        when(operationStorage.findOperationStatus(request.statusId()))
                .thenReturn(status);
        when(merchantStorage.findById(request.merchantId()))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(() -> operationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).isActive(request.merchantId(), MerchantStatus.ACTIVE);
        verify(operationStorage).findOperationType(request.typeId());
        verify(operationStorage).findOperationStatus(request.statusId());
        verify(merchantStorage).findById(request.merchantId());
    }

    @Test
    void create_shouldThrowForbiddenException_whenMerchantIsNotActive() {
        var request = createUpdateRequest();
        var message = "Merchant with id = %d is not active"
                .formatted(request.merchantId());
        doThrow(new EntityNotActiveException(message))
                .when(merchantStorage)
                .isActive(request.merchantId(), MerchantStatus.ACTIVE);
        assertThatThrownBy(() -> operationService.create(request))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(message);
        verify(merchantStorage)
                .isActive(request.merchantId(), MerchantStatus.ACTIVE);
        verify(merchantStorage, never()).findById(request.merchantId());
        verifyNoInteractions(operationStorage);
    }

    @Test
    void getById_shouldReturnOperationEntity() {
        var response = createOperationResponse();
        var operation = createOperationEntity(OperationStatus.NEW, OperationType.PAYMENT);
        var id = operation.getId();
        when(operationStorage.findById(id)).thenReturn(operation);
        when(dtoMapper.toResponse(operation)).thenReturn(response);
        var actualResponse = operationService.getById(id);
        assertThat(actualResponse).isSameAs(response);
        verify(operationStorage).findById(id);
        verify(dtoMapper).toResponse(operation);
    }

    @Test
    void getById_shouldThrowNotFoundException_whenOperationNotFound() {
        var id = 100L;
        var message = "Operation with id = %d does not exist".formatted(id);
        doThrow(new EntityNotFoundException(message))
                .when(operationStorage).findById(id);
        assertThatThrownBy(() -> operationService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(operationStorage).findById(id);
    }

    @Test
    void getAll_shouldReturnOperationsPage() {
        var operation1 = createOperationEntity(OperationStatus.NEW, OperationType.PAYMENT);
        var operation2 = createOperationEntity(OperationStatus.PAID, OperationType.PAYMENT);
        var response1 = createOperationResponse();
        var response2 = createOperationResponse();
        var pageable = PageRequest.of(0, 20);
        var operationPage = new PageImpl<>(
                List.of(operation1, operation2),
                pageable,
                2
        );
        when(operationStorage.findAll(pageable)).thenReturn(operationPage);
        when(dtoMapper.toResponse(operation1)).thenReturn(response1);
        when(dtoMapper.toResponse(operation2)).thenReturn(response2);
        var actualPage = operationService.getAll(pageable);
        verify(operationStorage).findAll(pageable);
        verify(dtoMapper).toResponse(operation1);
        verify(dtoMapper).toResponse(operation2);
        assertThat(actualPage.getContent()).containsExactly(response1, response2);
        assertThat(actualPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void processPayment_shouldUpdateOperation_whenStatusIsNew() {
        var request = createProcessOperationRequest();
        var operation = createOperationEntity(OperationStatus.NEW, OperationType.PAYMENT);
        var paidStatus = createOperationStatusEntity(OperationStatus.PAID);
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        when(operationStorage.findOperationStatus(OperationStatus.PAID))
                .thenReturn(paidStatus);
        operationService.processPayment(request);
        verify(operationStorage).findById(operation.getId());
        verify(operationStorage).findOperationStatus(OperationStatus.PAID);
        verify(operationStorage).save(operationCaptor.capture());
        var updatedOperation = operationCaptor.getValue();
        assertThat(updatedOperation.getId()).isEqualTo(operation.getId());
        assertThat(updatedOperation.getStatus()).isSameAs(paidStatus);
        assertThat(updatedOperation.getProcessedAt()).isNotNull();
    }

    @Test
    void processPayment_shouldThrowBadRequestException_whenStatusIsNotNew() {
        var request = createProcessOperationRequest();
        var paidOperation = createOperationEntity(OperationStatus.PAID, OperationType.PAYMENT);
        var message = "Operation with id = %d is not new"
                .formatted(paidOperation.getId());
        when(operationStorage.findById(paidOperation.getId()))
                .thenReturn(paidOperation);
        assertThatThrownBy(() -> operationService.processPayment(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(message);
        verify(operationStorage).findById(paidOperation.getId());
        verify(operationStorage, never()).save(any(OperationEntity.class));
    }

    @Test
    void processPayment_shouldThrowNotFoundException_whenOperationNotFound() {
        var request = createProcessOperationRequest();
        var id = 1L;
        var message = "Operation with id = %d does not exist".formatted(id);
        when(operationStorage.findById(id))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(() -> operationService.processPayment(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(operationStorage).findById(id);
        verify(operationStorage, never())
                .save(any(OperationEntity.class));
    }

    @Test
    void process_payment_shouldThrowBadRequestException_whenOperationSumInvalid() {
        var operation = createOperationEntity(OperationStatus.NEW, OperationType.PAYMENT);
        var invalidRequest = new ProcessOperationRequest(1L, 10000L);
        var message = "Invalid sum: provided = %s, must be = %s"
                .formatted(invalidRequest.sum(), operation.getSum());
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        assertThatThrownBy(() -> operationService.processPayment(invalidRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(message);
        verify(operationStorage).findById(operation.getId());
        verify(operationStorage, never())
                .save(any(OperationEntity.class));
    }

    @Test
    void processPayment_shouldThrowNotFoundException_whenPaidStatusNotFound() {
        var request = createProcessOperationRequest();
        var operation = createOperationEntity(OperationStatus.NEW, OperationType.PAYMENT);
        var message = "Operation status with code = PAID not found";
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        when(operationStorage.findOperationStatus(OperationStatus.PAID))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(() -> operationService.processPayment(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(operationStorage).findById(operation.getId());
        verify(operationStorage).findOperationStatus(OperationStatus.PAID);
        verify(operationStorage, never()).save(any(OperationEntity.class));
    }

    @Test
    void cancelOperation_shouldUpdateOperation_whenStatusIsNew() {
        var request = createProcessOperationRequest();
        var operation = createOperationEntity(OperationStatus.NEW, OperationType.PAYMENT);
        var rejectedStatus = createOperationStatusEntity(OperationStatus.REJECTED);
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        when(operationStorage.findOperationStatus(OperationStatus.REJECTED))
                .thenReturn(rejectedStatus);
        operationService.processCancel(request);
        verify(operationStorage).findById(operation.getId());
        verify(operationStorage).findOperationStatus(OperationStatus.REJECTED);
        verify(operationStorage).save(operationCaptor.capture());
        var updatedOperation = operationCaptor.getValue();
        assertThat(updatedOperation.getId()).isEqualTo(operation.getId());
        assertThat(updatedOperation.getStatus()).isSameAs(rejectedStatus);
        assertThat(updatedOperation.getProcessedAt()).isNotNull();
    }

    @Test
    void cancelOperation_shouldUpdateOperationAndCreateNew_whenStatusIsPaidOrCompleted() {
        var request = createProcessOperationRequest();
        var operation = createOperationEntity(OperationStatus.PAID, OperationType.PAYMENT);
        var newStatus = createOperationStatusEntity(OperationStatus.NEW);
        var refundedStatus = createOperationStatusEntity(OperationStatus.REFUNDED);
        var returnType = createOperationTypeEntity(OperationType.RETURN);
        when(operationStorage.findById(operation.getId())).thenReturn(operation);
        when(operationStorage.findOperationStatus(OperationStatus.NEW))
                .thenReturn(newStatus);
        when(operationStorage.findOperationStatus(OperationStatus.REFUNDED))
                .thenReturn(refundedStatus);
        when(operationStorage.findOperationType(OperationType.RETURN))
                .thenReturn(returnType);
        operationService.processCancel(request);
        verify(operationStorage).findById(operation.getId());
        verify(operationStorage).findOperationStatus(OperationStatus.REFUNDED);
        verify(operationStorage).findOperationStatus(OperationStatus.NEW);
        verify(operationStorage).findOperationType(OperationType.RETURN);
        verify(operationStorage, times(2)).save(operationCaptor.capture());
        var savedOperations = operationCaptor.getAllValues();
        var updatedOperation = savedOperations.getFirst();
        assertThat(updatedOperation.getId()).isEqualTo(operation.getId());
        assertThat(updatedOperation.getStatus()).isSameAs(refundedStatus);
        assertThat(updatedOperation.getProcessedAt()).isNotNull();
        var refundOperation = savedOperations.get(1);
        assertThat(refundOperation.getParentId()).isEqualTo(updatedOperation.getId());
        assertThat(refundOperation.getStatus()).isSameAs(newStatus);
        assertThat(refundOperation.getType()).isSameAs(returnType);
    }

    @Test
    void cancelOperation_shouldThrowBadRequestException_whenOperationStatusIsInvalid() {
        var request = createProcessOperationRequest();
        var operation = createOperationEntity(OperationStatus.REJECTED, OperationType.PAYMENT);
        var message = "Cannot cancel operation with REJECTED status";
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        assertThatThrownBy(
                () -> operationService.processCancel(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(message);
        verify(operationStorage).findById(operation.getId());
        verify(operationStorage, never()).save(any(OperationEntity.class));
    }

    private OperationEntity createOperationEntity(OperationStatus status, OperationType type) {
        return OperationEntity
                .builder()
                .id(1L)
                .merchant(createMerchantEntity())
                .status(createOperationStatusEntity(status))
                .sum(100000L)
                .type(createOperationTypeEntity(type))
                .createdAt(LocalDateTime.now())
                .build();
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

    private OperationStatusEntity createOperationStatusEntity(OperationStatus status) {
        return new OperationStatusEntity(
                1L,
                status,
                status.name().charAt(0) +
                        status.name().substring(1).toLowerCase());
    }

    private OperationTypeEntity createOperationTypeEntity(OperationType type) {
        return new OperationTypeEntity(
                1L,
                type,
                type.name().charAt(0) +
                        type.name().substring(1).toLowerCase());
    }

    private GetOperationResponse createOperationResponse() {
        return new GetOperationResponse(
                1L,
                UUID.randomUUID(),
                "test",
                "New",
                new BigDecimal("1000.00"),
                "Payment",
                null,
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 1, 0, 0));
    }

    private CreateOperationRequest createUpdateRequest() {
        return new CreateOperationRequest(
                1L,
                1L,
                100000L,
                1L,
                null,
                LocalDateTime.of(2026, 1, 1, 0, 0));
    }

    private ProcessOperationRequest createProcessOperationRequest() {
        return new ProcessOperationRequest(1L, 100000L);
    }

}
