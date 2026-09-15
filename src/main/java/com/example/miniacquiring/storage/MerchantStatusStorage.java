package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import com.example.miniacquiring.storage.repository.MerchantStatusRepository;
import org.springframework.stereotype.Component;

@Component
public class MerchantStatusStorage {

    private MerchantStatusRepository merchantStatusRepository;

    public MerchantStatusEntity findById(Long id) {
        return merchantStatusRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation status with id = %d not found".formatted(id))
        );
    }

}
