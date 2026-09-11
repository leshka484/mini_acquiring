package com.example.miniacquiring.service;

import com.example.miniacquiring.core.dto.merchant.CreateMerchantRequest;
import com.example.miniacquiring.core.dto.merchant.GetMerchantResponse;
import com.example.miniacquiring.core.dto.merchant.UpdateMerchantRequest;
import com.example.miniacquiring.core.mapper.EntityMapper;
import com.example.miniacquiring.storage.CommissionTypeStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.math.BigDecimal;
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
    private final EntityMapper entityMapper;

    public GetMerchantResponse create(CreateMerchantRequest request) {
        var type = commissionTypeStorage.findById(request.commissionTypeId());
        var merchant = buildMerchantEntity(request.name(), request.commissionValue(), type);
        merchantStorage.save(merchant);
        log.info("Merchant {} created", merchant.getName());
        return entityMapper.toResponse(merchant);
    }

    public GetMerchantResponse getById(Long id) {
        var merchant = merchantStorage.findById(id);
        log.info("Merchant with id = {} found", id);
        return entityMapper.toResponse(merchant);
    }

    public GetMerchantResponse getByName(String name) {
        var merchant = merchantStorage.findByName(name);
        log.info("Merchant {} found", name);
        return entityMapper.toResponse(merchant);
    }

    public Page<GetMerchantResponse> getAll(Pageable pageable) {
        var merchants = merchantStorage.findAll(pageable);
        log.info("Getting all merchants");
        return merchants.map(entityMapper::toResponse);
    }

    public GetMerchantResponse update(Long id, UpdateMerchantRequest request) {
        merchantStorage.findById(id);
        var type = commissionTypeStorage.findById(request.commissionTypeId());
        var merchant = buildMerchantEntity(id,
                request.name(),
                request.commissionValue(),
                type);
        merchantStorage.save(merchant);
        log.info("Merchant {} updated", merchant.getName());
        return entityMapper.toResponse(merchant);
    }

    public void deleteById(List<Long> ids) {
        merchantStorage.deleteById(ids);
        log.info("Merchants deleted");
    }

    public void deleteById(Long id) {
        merchantStorage.deleteById(id);
    }

    private MerchantEntity buildMerchantEntity(Long id,
                                               String name,
                                               BigDecimal commissionValue,
                                               CommissionTypeEntity commissionType) {
        return MerchantEntity
                .builder()
                .id(id)
                .name(name)
                .commissionValue(commissionValue)
                .commissionType(commissionType)
                .build();
    }

    private MerchantEntity buildMerchantEntity(String name,
                                               BigDecimal commissionValue,
                                               CommissionTypeEntity commissionType) {
        return MerchantEntity
                .builder()
                .name(name)
                .commissionValue(commissionValue)
                .commissionType(commissionType)
                .build();
    }

}