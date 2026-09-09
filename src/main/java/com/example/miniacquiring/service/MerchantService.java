package com.example.miniacquiring.service;

import com.example.miniacquiring.core.dto.merchant.MerchantCreateDto;
import com.example.miniacquiring.core.dto.merchant.MerchantGetDto;
import com.example.miniacquiring.core.dto.merchant.MerchantUpdateDto;
import com.example.miniacquiring.core.mapper.MerchantMapper;
import com.example.miniacquiring.storage.CommissionTypeStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantStorage merchantStorage;
    private final CommissionTypeStorage commissionTypeStorage;
    private final MerchantMapper merchantMapper;

    public MerchantGetDto create(MerchantCreateDto request) {
        var type = commissionTypeStorage.findById(request.getCommissionTypeId());
        var merchant = buildMerchantEntity(request.getName(), request.getCommissionValue(), type);
        merchantStorage.save(merchant);
        log.info("Merchant {} created", merchant.getName());
        return merchantMapper.toResponse(merchant);
    }

    public MerchantGetDto getById(Long id) {
        var merchant = merchantStorage.getById(id);
        log.info("Merchant with id = {} found", id);
        return merchantMapper.toResponse(merchant);
    }

    public MerchantGetDto getByName(String name) {
        var merchant = merchantStorage.getByName(name);
        log.info("Merchant {} found", name);
        return merchantMapper.toResponse(merchant);
    }

    public List<MerchantGetDto> getAll() {
        var merchants = merchantStorage.getAll();
        log.info("Getting all merchants");
        return merchantMapper.toResponse(merchants);
    }

    public MerchantGetDto update(Long id, MerchantUpdateDto request) {
        merchantStorage.getById(id);
        var type = commissionTypeStorage.findById(request.getCommissionTypeId());
        var merchant = buildMerchantEntity(id,
                request.getName(),
                request.getCommissionValue(),
                type);
        merchantStorage.save(merchant);
        log.info("Merchant {} updated", merchant.getName());
        return merchantMapper.toResponse(merchant);
    }

    public void deleteById(List<Long> ids) {
        merchantStorage.deleteById(ids, "Some merchants do not exist");
        log.info("Merchants deleted");
    }

    public void deleteById(Long id) {
        merchantStorage.deleteById(id, "Merchant with id = %d does not exist".formatted(id));
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