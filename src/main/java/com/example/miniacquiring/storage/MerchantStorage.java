package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.repository.MerchantRepository;

public class MerchantStorage extends BaseStorage<MerchantEntity, Long, MerchantRepository> {

    public MerchantStorage(MerchantRepository merchantRepository) {
        super(merchantRepository);
    }

    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public MerchantEntity getById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id))
        );
    }

    public MerchantEntity getByName(String name) {
        return repository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Merchant with name '%s' not found".formatted(name))
        );
    }

    public MerchantEntity save(MerchantEntity merchant) {
        return repository.save(merchant);
    }

}
