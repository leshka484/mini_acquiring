package com.example.miniacquiring.service;

import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.MerchantStatusStorage;
import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantStatusService {

    private final MerchantStatusStorage merchantStatusStorage;

    public MerchantStatusEntity getById(Long id) {
        try {
            log.info("Getting merchant status by id");
            return merchantStatusStorage.findById(id);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
