package com.example.miniacquiring;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetMerchantResponse;
import com.example.miniacquiring.core.dto.MerchantFilter;
import com.example.miniacquiring.core.dto.UpsertMerchantRequest;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.ForbiddenException;
import com.example.miniacquiring.core.exception.MerchantNotActiveException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.service.MerchantService;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import com.example.miniacquiring.storage.repository.MerchantStatusRepository;
import java.math.BigDecimal;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

    @Mock
    private MerchantStorage merchantStorage;

    @Mock
    private CommissionTypeRepository commissionTypeRepository;

    @Mock
    private MerchantStatusRepository merchantStatusRepository;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private MerchantService merchantService;

    @Captor
    private ArgumentCaptor<MerchantEntity> merchantCaptor;

    @Test
    void create_shouldCreateNewMerchant() {
        var commissionType = createCommissionType();
        var merchantStatus = createMerchantStatus();
        var request = createUpsertRequest();
        when(commissionTypeRepository.findById(request.commissionTypeId()))
                .thenReturn(Optional.of(commissionType));
        when(merchantStatusRepository.findById(request.statusId()))
                .thenReturn(Optional.of(merchantStatus));
        merchantService.create(request);
        verify(merchantStatusRepository).findById(request.statusId());
        verify(commissionTypeRepository).findById(request.commissionTypeId());
        verify(merchantStorage).save(merchantCaptor.capture());
        var savedMerchant = merchantCaptor.getValue();
        assertThat(savedMerchant.getName()).isEqualTo(request.name());
        assertThat(savedMerchant.getCommissionValue()).isEqualByComparingTo(request.commissionValue());
        assertThat(savedMerchant.getCommissionType()).isSameAs(commissionType);
        assertThat(savedMerchant.getStatus()).isSameAs(merchantStatus);
        assertThat(savedMerchant.getId()).isNull();
    }

    @Test
    void create_shouldThrowNotFoundException_whenTypeNotFound() {
        var request = createUpsertRequest();
        when(commissionTypeRepository.findById(request.commissionTypeId()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> merchantService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(
                        "Commission type with id = 1 not found"
                );
        verify(merchantStorage, never()).save(any());
        verify(merchantStatusRepository, never()).findById(any());
    }

    @Test
    void create_shouldThrowNotFoundException_whenStatusNotFound() {
        var commissionType = createCommissionType();
        var request = createUpsertRequest();
        when(commissionTypeRepository.findById(request.commissionTypeId()))
                .thenReturn(Optional.of(commissionType));
        when(merchantStatusRepository.findById(request.statusId()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> merchantService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(
                        "Merchant status with id = 1 not found"
                );
        verify(merchantStorage, never()).save(any());
    }

    @Test
    void getById_shouldReturnMerchant() {
        var merchant = createMerchantEntity();
        when(merchantStorage.findById(merchant.getId())).thenReturn(merchant);
        var actualMerchant = merchantService.getById(merchant.getId());
        assertThat(actualMerchant).isSameAs(merchant);
        verify(merchantStorage).findById(merchant.getId());
    }

    @Test
    void getById_shouldThrowNotFoundException_whenMerchantDoesNotExist() {
        var id = 100L;
        var message = "Merchant with id = %d not found".formatted(id);
        when(merchantStorage.findById(id))
                .thenThrow(new EntityNotFoundException(message));
        assertThatThrownBy(() -> merchantService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(message);
        verify(merchantStorage).findById(id);
    }

    @Test
    void getFilteredMerchants_shouldReturnMappedPage() {
        var filter = new MerchantFilter(
                1L,
                "test",
                1L,
                1L,
                new BigDecimal("9.00"),
                new BigDecimal("11.00"));
        var merchant = createMerchantEntity();
        var pageable = PageRequest.of(0, 20);
        var merchantPage = new PageImpl<>(
                List.of(merchant),
                pageable,
                1
        );
        var response = mock(GetMerchantResponse.class);
        when(merchantStorage.getFilteredMerchants(filter, pageable)).thenReturn(merchantPage);
        when(dtoMapper.toResponse(merchant)).thenReturn(response);
        var actualPage = merchantService.getFilteredMerchants(filter, pageable);
        assertThat(actualPage.getContent()).containsExactly(response);
        assertThat(actualPage.getTotalElements()).isEqualTo(1);
        verify(merchantStorage).getFilteredMerchants(filter, pageable);
        verify(dtoMapper).toResponse(merchant);
    }

    @Test
    void update_shouldUpdateMerchant() {
        var commissionType = createCommissionType();
        var merchantStatus = createMerchantStatus();
        var request = new UpsertMerchantRequest(
                "updated",
                new BigDecimal("100.00"),
                commissionType.getId(),
                merchantStatus.getId());
        var merchant = createMerchantEntity();
        var id = merchant.getId();
        when(merchantStorage.findById(id)).thenReturn(merchant);
        when(commissionTypeRepository.findById(request.commissionTypeId()))
                .thenReturn(Optional.of(commissionType));
        when(merchantStatusRepository.findById(request.statusId()))
                .thenReturn(Optional.of(merchantStatus));
        merchantService.update(id, request);
        verify(merchantStorage).isActive(id);
        verify(commissionTypeRepository).findById(request.commissionTypeId());
        verify(merchantStatusRepository).findById(request.statusId());
        verify(merchantStorage).save(merchantCaptor.capture());
        var updatedMerchant = merchantCaptor.getValue();
        assertThat(updatedMerchant.getId()).isEqualTo(id);
        assertThat(updatedMerchant.getName()).isEqualTo(request.name());
        assertThat(updatedMerchant.getCommissionValue())
                .isEqualByComparingTo(request.commissionValue());
        assertThat(updatedMerchant.getCommissionType()).isSameAs(commissionType);
        assertThat(updatedMerchant.getStatus()).isSameAs(merchantStatus);
    }

    @Test
    void update_shouldThrowForbiddenException_whenMerchantIsNotActive() {
        var id = 1L;
        var request = createUpsertRequest();
        var message = "Merchant with id = %d is not active".formatted(id);

        doThrow(new MerchantNotActiveException(message))
                .when(merchantStorage)
                .isActive(id);
        assertThatThrownBy(
                () -> merchantService.update(id, request))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(message);
        verify(merchantStorage).isActive(id);
        verify(merchantStorage, never()).findById(any());
        verifyNoInteractions(
                commissionTypeRepository,
                merchantStatusRepository
        );
        verify(merchantStorage, never()).save(any());
    }

    @Test
    void deleteById_shouldDeleteMerchant() {
        var ids = List.of(1L, 2L, 3L);
        merchantService.deleteById(ids);
        verify(merchantStorage).deleteById(ids);
    }

    private CommissionTypeEntity createCommissionType() {
        return CommissionTypeEntity
                .builder()
                .id(1L)
                .type(Const.PERCENTAGE_COMMISSION)
                .build();
    }

    private MerchantStatusEntity createMerchantStatus() {
        return MerchantStatusEntity
                .builder()
                .id(1L)
                .status(Const.MERCHANT_ACTIVE_STATUS)
                .build();
    }

    private UpsertMerchantRequest createUpsertRequest() {
        return new UpsertMerchantRequest(
                "test",
                new BigDecimal("10.00"),
                1L,
                1L);
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

}
