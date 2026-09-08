package com.example.miniacquiring.service;

import com.example.miniacquiring.storage.CommissionTypeStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.repository.CommissionRepository;
import com.example.miniacquiring.storage.repository.MerchantRepository;
import com.example.miniacquiring.storage.repository.OperationRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantStorage merchantStorage;
    private final CommissionTypeStorage commissionTypeStorage;

    public MerchantEntity create(String name, BigDecimal commissionValue, String commissionType) {
        CommissionTypeEntity type = commissionTypeStorage.findByType(commissionType);
        MerchantEntity merchant = MerchantEntity
                .builder()
                .name(name)
                .commissionValue(commissionValue)
                .commissionType(type)
                .build();
        log.info("Merchant {} created", merchant.getName());
        return merchantRepository.save(merchant);
    }

    public MerchantEntity getById(Long id) {
        MerchantEntity merchant = merchantStorage.getById(id);
        log.info("Merchant with id = {} found", id);
        return merchant;
    }

    public MerchantEntity getByName(String name) {
        MerchantEntity merchant = merchantStorage.getByName(name);
        log.info("Merchant {} found", name);
        return merchant;
    }

    public void update(Long id, String name, BigDecimal commissionValue, CommissionTypeEntity commissionType) {
        if (merchantStorage.existsById(id)) {
            MerchantEntity merchant = MerchantEntity
                    .builder()
                    .id(id)
                    .name(name)
                    .commissionValue(commissionValue)
                    .commissionType(commissionType)
                    .build();
            log.info("Merchant {} updated", merchant.getName());
            merchantRepository.save(merchant);
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

}