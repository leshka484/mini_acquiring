package com.example.miniacquiring.service;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetMerchantResponse;
import com.example.miniacquiring.core.dto.MerchantFilter;
import com.example.miniacquiring.core.dto.UpsertMerchantRequest;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.ForbiddenException;
import com.example.miniacquiring.core.exception.MerchantNotActiveException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.CommissionTypeStorage;
import com.example.miniacquiring.storage.MerchantStatusStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.List;
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
    private final CommissionTypeStorage commissionTypeStorage;
    private final MerchantStatusStorage merchantStatusStorage;
    private final DtoMapper dtoMapper;

    public void create(UpsertMerchantRequest request) {
        try {
            log.info("Creating merchant");
            createMerchantEntity(request);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public GetMerchantResponse getById(Long id) {
        try {
            log.info("Trying to find merchant with id = {}", id);
            return dtoMapper.toResponse(merchantStorage.findById(id));
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
            merchantStorage.isActive(id);
            updateMerchantEntity(id, request);
        } catch (MerchantNotActiveException exception) {
            throw new ForbiddenException(exception.getMessage());
        }
    }

    public void deleteById(List<Long> ids) {
        log.info("Deleting some merchants");
        merchantStorage.deleteById(ids);
    }

    public void deleteById(Long id) {
        log.info("Deleting merchant");
        merchantStorage.deleteById(id);
    }

    private void updateMerchantEntity(Long id, UpsertMerchantRequest request) {
        var type = commissionTypeStorage.findById(request.commissionTypeId());
        var status = merchantStatusStorage.findById(request.statusId());
        var merchant = MerchantEntity
                .builder()
                .id(id)
                .name(request.name())
                .commissionValue(request.commissionValue())
                .commissionType(type)
                .status(status)
                .build();
        merchantStorage.save(merchant);
    }

    private void createMerchantEntity(UpsertMerchantRequest request) {
        var type = commissionTypeStorage.findById(request.commissionTypeId());
        var status = merchantStatusStorage.findById(request.statusId());
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