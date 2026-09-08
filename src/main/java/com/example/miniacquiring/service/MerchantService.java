package com.example.miniacquiring.service;

import com.example.miniacquiring.storage.CommissionTypeStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantStorage merchantStorage;
    private final CommissionTypeStorage commissionTypeStorage;

    public MerchantEntity create(String name, BigDecimal commissionValue, String commissionType) {
        var type = commissionTypeStorage.findByType(commissionType);
        var merchant = buildMerchantEntity(name, commissionValue, type);
        log.info("Merchant {} created", merchant.getName());
        return merchantStorage.save(merchant);
    }

    public MerchantEntity getById(Long id) {
        var merchant = merchantStorage.getById(id);
        log.info("Merchant with id = {} found", id);
        return merchant;
    }

    public MerchantEntity getByName(String name) {
        var merchant = merchantStorage.getByName(name);
        log.info("Merchant {} found", name);
        return merchant;
    }

    public void update(Long id, String name, BigDecimal commissionValue, CommissionTypeEntity commissionType) {
        if (merchantStorage.existsById(id)) {
            var merchant = buildMerchantEntity(id, name, commissionValue, commissionType);
            log.info("Merchant {} updated", merchant.getName());
        }
    }

    public void deleteById(Long id) {
        merchantStorage.deleteById(id);
        log.info("Merchant with id = {} deleted", id);
    }

    public void deleteByName(String name) {
        merchantStorage.deleteByName(name);
        log.info("Merchant {} deleted", name);
    }

    private MerchantEntity buildMerchantEntity(Long id, String name, BigDecimal commissionValue, CommissionTypeEntity commissionType) {
        var merchant = MerchantEntity
                .builder()
                .id(id)
                .name(name)
                .commissionValue(commissionValue)
                .commissionType(commissionType)
                .build();
        return merchantStorage.save(merchant);
    }

    private MerchantEntity buildMerchantEntity(String name, BigDecimal commissionValue, CommissionTypeEntity commissionType) {
        var merchant = MerchantEntity
                .builder()
                .name(name)
                .commissionValue(commissionValue)
                .commissionType(commissionType)
                .build();
        return merchantStorage.save(merchant);
    }

}