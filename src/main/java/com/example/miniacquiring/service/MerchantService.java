package com.example.miniacquiring.service;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetMerchantResponse;
import com.example.miniacquiring.core.dto.MerchantFilter;
import com.example.miniacquiring.core.dto.UpsertMerchantRequest;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantService {

    private final MerchantStorage merchantStorage;
    private final ReferenceDataService referenceDataService;
    private final DtoMapper dtoMapper;

    public void create(UpsertMerchantRequest request) {
        try {
            log.info("Creating merchant");
            createMerchantEntity(request);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public MerchantEntity getMerchantEntityById(Long id) {
        try {
            log.info("Trying to find merchant with id = {}", id);
            return merchantStorage.findById(id);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public Page<GetMerchantResponse> getFilteredMerchants(MerchantFilter filter, Pageable pageable) {
        log.info("Getting merchants with filter");
        return merchantStorage.getFilteredMerchants(filter, pageable)
                .map(dtoMapper::toResponse);
    }

    public void update(Long id, UpsertMerchantRequest request) {
        try {
            log.info("Updating merchant {}", request.name());
            updateMerchantEntity(id, request);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public void deleteById(List<Long> ids) {
        log.info("Deleting some merchants");
        merchantStorage.deleteById(ids);
    }

    public Long getIdByPublicId(UUID publicId) {
        try {
            log.info("Getting merchant id by it's public id = {}", publicId);
            return merchantStorage.findIdByPublicId(publicId);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    private void updateMerchantEntity(Long id, UpsertMerchantRequest request) {
        var type = referenceDataService.getCommissionType(request.commissionTypeId());
        var status = referenceDataService.getMerchantStatus(request.statusId());
        var merchant = getMerchantEntityById(id);
        var updated = merchant.toBuilder()
                .name(request.name())
                .commissionValue(request.commissionValue())
                .commissionType(type)
                .status(status)
                .build();
        merchantStorage.save(updated);
    }

    private void createMerchantEntity(UpsertMerchantRequest request) {
        var type = referenceDataService.getCommissionType(request.commissionTypeId());
        var status = referenceDataService.getMerchantStatus(request.statusId());
        var merchant = MerchantEntity
                .builder()
                .name(request.name())
                .commissionValue(request.commissionValue())
                .commissionType(type)
                .status(status)
                .build();
        merchantStorage.save(merchant);
    }

}