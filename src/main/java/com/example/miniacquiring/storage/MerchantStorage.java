package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MerchantStorage {

    private final MerchantRepository merchantRepository;

    public Boolean existsById(Long id) {
        return merchantRepository.existsById(id);
    }

    public MerchantEntity getById(Long id) {
        return merchantRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id))
        );
    }

    public MerchantEntity getByName(String name) {
        return merchantRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Merchant with name '%s' not found".formatted(name))
        );
    }

    public void deleteById(Long id) {
        if (!merchantRepository.existsById(id)) {
            throw new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id));
        }
        merchantRepository.deleteById(id);
    }

    public void deleteByName(String name) {
        if (!merchantRepository.existsByName(name)) {
            throw new IllegalArgumentException("Merchant with id = %s does not exist".formatted(name));
        }
        merchantRepository.deleteByName(name);
    }

}
