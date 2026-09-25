package com.example.miniacquiring;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.PayRequest;
import com.example.miniacquiring.core.dto.UpsertOperationRequest;
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
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

    @Mock
    private OperationStorage operationStorage;

    @Mock
    private OperationTypeRepository operationTypeRepository;

    @Mock
    private OperationStatusRepository operationStatusRepository;

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
        var request = createUpsertRequest();
        var operationType = createOperationTypeEntity();
        var operationStatus = createOperationStatusEntity(OperationStatus.NEW);
        var merchant = createMerchantEntity();
        when(operationTypeRepository.findById(request.typeId()))
                .thenReturn(Optional.of(operationType));
        when(operationStatusRepository.findById(request.statusId()))
                .thenReturn(Optional.of(operationStatus));
        when(merchantStorage.findById(request.merchantId()))
                .thenReturn(merchant);
        operationService.create(request);
        verify(operationTypeRepository).findById(request.typeId());
        verify(operationStatusRepository).findById(request.statusId());
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
        var request = createUpsertRequest();
        var message = "Operation type with id = %d not found".formatted(request.typeId());
        when(operationTypeRepository.findById(request.typeId()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> operationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).isActive(request.merchantId(), MerchantStatus.ACTIVE);
        verify(operationTypeRepository).findById(request.typeId());
        verifyNoInteractions(operationStatusRepository);
        verifyNoInteractions(operationStorage);
        verify(merchantStorage, never()).findById(request.merchantId());
    }

    @Test
    void create_shouldThrowNotFoundException_whenStatusNotFound() {
        var request = createUpsertRequest();
        var type = createOperationTypeEntity();
        var message = "Operation status with id = %d not found".formatted(request.statusId());
        when(operationTypeRepository.findById(request.typeId()))
                .thenReturn(Optional.of(type));
        when(operationStatusRepository.findById(request.statusId()))
                .thenReturn((Optional.empty()));
        assertThatThrownBy(() -> operationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).isActive(request.merchantId(), MerchantStatus.ACTIVE);
        verify(operationTypeRepository).findById(request.typeId());
        verify(operationStatusRepository).findById(request.statusId());
        verify(merchantStorage, never()).findById(request.merchantId());
        verifyNoInteractions(operationStorage);
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenMerchantNotFound() {
        var request = createUpsertRequest();
        var type = createOperationTypeEntity();
        var status = createOperationStatusEntity(OperationStatus.NEW);
        var message = "Merchant with id = %d not found".formatted(request.merchantId());
        when(operationTypeRepository.findById(request.typeId()))
                .thenReturn(Optional.of(type));
        when(operationStatusRepository.findById(request.statusId()))
                .thenReturn((Optional.of(status)));
        when(merchantStorage.findById(request.merchantId()))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(() -> operationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).isActive(request.merchantId(), MerchantStatus.ACTIVE);
        verify(operationTypeRepository).findById(request.typeId());
        verify(operationStatusRepository).findById(request.statusId());
        verify(merchantStorage).findById(request.merchantId());
        verifyNoInteractions(operationStorage);
    }

    @Test
    void create_shouldThrowForbiddenException_whenMerchantIsNotActive() {
        var request = createUpsertRequest();
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
        var operation = createOperationEntity(OperationStatus.NEW);
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
        var operation1 = createOperationEntity(OperationStatus.NEW);
        var operation2 = createOperationEntity(OperationStatus.PAID);
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
        var request = createPayRequest();
        var operation = createOperationEntity(OperationStatus.NEW);
        var paidStatus = createOperationStatusEntity(OperationStatus.PAID);
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        when(operationStatusRepository.findByCode(OperationStatus.PAID))
                .thenReturn(Optional.of(paidStatus));
        operationService.processPayment(request);
        verify(operationStorage).findById(operation.getId());
        verify(operationStatusRepository).findByCode(OperationStatus.PAID);
        verify(operationStorage).save(operationCaptor.capture());
        var updatedOperation = operationCaptor.getValue();
        assertThat(updatedOperation.getId()).isEqualTo(operation.getId());
        assertThat(updatedOperation.getStatus()).isSameAs(paidStatus);
        assertThat(updatedOperation.getProcessedAt()).isNotNull();
    }

    @Test
    void processPayment_shouldThrowBadRequestException_whenStatusIsNotNew() {
        var request = createPayRequest();
        var paidOperation = createOperationEntity(OperationStatus.PAID);
        var message = "Operation with id = %d is not NEW"
                .formatted(paidOperation.getId());
        when(operationStorage.findById(paidOperation.getId()))
                .thenReturn(paidOperation);
        assertThatThrownBy(() -> operationService.processPayment(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(message);
        verify(operationStorage).findById(paidOperation.getId());
        verifyNoInteractions(operationStatusRepository);
        verify(operationStorage, never()).save(any(OperationEntity.class));
    }

    @Test
    void processPayment_shouldThrowNotFoundException_whenOperationNotFound() {
        var request = createPayRequest();
        var id = 1L;
        var message = "Operation with id = %d does not exist".formatted(id);
        when(operationStorage.findById(id))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(() -> operationService.processPayment(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(operationStorage).findById(id);
        verifyNoInteractions(operationStatusRepository);
        verify(operationStorage, never())
                .save(any(OperationEntity.class));
    }

    @Test
    void process_payment_shouldThrowBadRequestException_whenOperationSumInvalid() {
        var operation = createOperationEntity(OperationStatus.NEW);
        var invalidRequest = new PayRequest(1L, new BigDecimal("100.00"));
        var message = "Payment has invalid sum = %s, must be sum = %s"
                .formatted(invalidRequest.sum(), operation.getSum());
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        assertThatThrownBy(() -> operationService.processPayment(invalidRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(message);
        verify(operationStorage).findById(operation.getId());
        verifyNoInteractions(operationStatusRepository);
        verify(operationStorage, never())
                .save(any(OperationEntity.class));
    }

    @Test
    void processPayment_shouldThrowNotFoundException_whenPaidStatusNotFound() {
        var request = createPayRequest();
        var operation = createOperationEntity(OperationStatus.NEW);
        var message = "Operation status with code = PAID not found";
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        when(operationStatusRepository.findByCode(OperationStatus.PAID))
                .thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> operationService.processPayment(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(operationStorage).findById(operation.getId());
        verify(operationStatusRepository).findByCode(OperationStatus.PAID);
        verify(operationStorage, never()).save(any(OperationEntity.class));
    }

    @Test
    void cancelOperation_shouldUpdateOperation_whenStatusIsNew() {
        var id = 1L;
        var operation = createOperationEntity(OperationStatus.NEW);
        var failedStatus = createOperationStatusEntity(OperationStatus.FAILED);
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        when(operationStatusRepository.findByCode(OperationStatus.FAILED))
                .thenReturn(Optional.of(failedStatus));
        operationService.cancelOperation(id);
        verify(operationStorage).findById(operation.getId());
        verify(operationStatusRepository).findByCode(OperationStatus.FAILED);
        verify(operationStorage).save(operationCaptor.capture());
        var updatedOperation = operationCaptor.getValue();
        assertThat(updatedOperation.getId()).isEqualTo(operation.getId());
        assertThat(updatedOperation.getStatus()).isSameAs(failedStatus);
        assertThat(updatedOperation.getProcessedAt()).isNotNull();
    }

    @Test
    void cancelOperation_shouldThrowBadRequestException_whenOperationStatusIsInvalid() {
        var operation = createOperationEntity(OperationStatus.PAID);
        var message = "Operation with id = %d is not %s"
                .formatted(operation.getId(), OperationStatus.NEW);
        when(operationStorage.findById(operation.getId()))
                .thenReturn(operation);
        assertThatThrownBy(
                () -> operationService.cancelOperation(operation.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(message);
        verify(operationStorage).findById(operation.getId());
        verifyNoInteractions(operationStatusRepository);
        verify(operationStorage, never()).save(any(OperationEntity.class));
    }

    @Test
    void update_shouldUpdateOperation() {
        var operation = createOperationEntity(OperationStatus.NEW);
        var id = operation.getId();
        var updateRequest = new UpsertOperationRequest(
                1L,
                1L,
                new BigDecimal("2000.00"),
                1L,
                null,
                LocalDateTime.of(2026, 2, 2, 0, 0));
        var newType = new OperationTypeEntity(
                2L,
                OperationType.RETURN,
                "Return");
        var newStatus = new OperationStatusEntity(
                2L,
                OperationStatus.PAID,
                "Paid");

        when(operationTypeRepository.findById(updateRequest.typeId()))
                .thenReturn(Optional.of(newType));
        when(operationStatusRepository.findById(updateRequest.statusId()))
                .thenReturn(Optional.of(newStatus));
        when(operationStorage.findById(id)).thenReturn(operation);
        operationService.update(id, updateRequest);
        verify(operationStorage).save(operationCaptor.capture());
        var updatedOperation = operationCaptor.getValue();
        verify(operationStorage).findById(id);
        verify(operationTypeRepository).findById(updateRequest.typeId());
        verify(operationStatusRepository).findById(updateRequest.statusId());
        assertThat(updatedOperation.getId()).isEqualTo(operation.getId());
        assertThat(updatedOperation.getStatus()).isSameAs(newStatus);
        assertThat(updatedOperation.getType()).isSameAs(newType);
        assertThat(updatedOperation.getParentId()).isEqualTo(updateRequest.parentId());
        assertThat(updatedOperation.getProcessedAt()).isEqualTo(updateRequest.processedAt());
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenTypeNotFound() {
        var id = 1L;
        var request = createUpsertRequest();
        var message = "Operation type with id = %d not found".formatted(request.typeId());
        when(operationTypeRepository.findById(request.typeId()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> operationService.update(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(operationTypeRepository).findById(request.typeId());
        verifyNoInteractions(operationStatusRepository);
        verifyNoInteractions(operationStorage);
    }

    @Test
    void update_shouldThrowNotFoundException_whenStatusNotFound() {
        var id = 1L;
        var request = createUpsertRequest();
        var type = createOperationTypeEntity();
        var message = "Operation status with id = %d not found".formatted(request.statusId());
        when(operationTypeRepository.findById(request.typeId()))
                .thenReturn(Optional.of(type));
        when(operationStatusRepository.findById(request.statusId()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> operationService.update(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(operationTypeRepository).findById(request.typeId());
        verify(operationStatusRepository).findById(request.statusId());
        verifyNoInteractions(operationStorage);
    }

    @Test
    void update_shouldThrowNotFoundException_whenOperationNotFound() {
        var id = 100L;
        var request = createUpsertRequest();
        var message = "Operation with id = %d not found".formatted(id);
        var type = createOperationTypeEntity();
        var status = createOperationStatusEntity(OperationStatus.NEW);
        when(operationTypeRepository.findById(request.typeId()))
                .thenReturn(Optional.of(type));
        when(operationStatusRepository.findById(request.statusId()))
                .thenReturn(Optional.of(status));
        when(operationStorage.findById(id))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(() -> operationService.update(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(operationTypeRepository).findById(request.typeId());
        verify(operationStatusRepository).findById(request.statusId());
        verify(operationStorage).findById(id);
        verify(operationStorage, never()).save(any());
}

    private OperationEntity createOperationEntity(OperationStatus status) {
        return OperationEntity
                .builder()
                .id(1L)
                .merchant(createMerchantEntity())
                .status(createOperationStatusEntity(status))
                .sum(new BigDecimal("1000.00"))
                .type(createOperationTypeEntity())
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

    private OperationTypeEntity createOperationTypeEntity() {
        return new OperationTypeEntity(
                1L,
                OperationType.PAYMENT,
                "Payment");
    }

    private GetOperationResponse createOperationResponse() {
        return new GetOperationResponse(
                "test",
                "New",
                new BigDecimal("1000.00"),
                "Payment",
                null,
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 1, 0, 0));
    }

    private UpsertOperationRequest createUpsertRequest() {
        return new UpsertOperationRequest(
                1L,
                1L,
                new BigDecimal("1000.00"),
                1L,
                null,
                LocalDateTime.of(2026, 1, 1, 0, 0));
    }

    private PayRequest createPayRequest() {
        return new PayRequest(1L, new BigDecimal("1000.00"));
    }

}
