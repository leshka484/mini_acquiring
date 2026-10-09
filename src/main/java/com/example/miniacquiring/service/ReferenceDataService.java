package com.example.miniacquiring.service;

import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import com.example.miniacquiring.storage.repository.MerchantStatusRepository;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferenceDataService {

    private final CommissionTypeRepository commissionTypeRepository;
    private final MerchantStatusRepository merchantStatusRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final OperationStatusRepository operationStatusRepository;

    @Cacheable(cacheNames = "operationTypes", key = "#id")
    public OperationTypeEntity getOperationType(Long id) {
        return operationTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation type with id = %d not found".formatted(id)));
    }

    @Cacheable(cacheNames = "operationStatuses", key = "#id")
    public OperationStatusEntity getOperationStatus(Long id) {
        return operationStatusRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation status with id = %d not found".formatted(id)));
    }

    @Cacheable(cacheNames = "commissionTypes", key = "#id")
    public CommissionTypeEntity getCommissionType(Long id) {
        return commissionTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Commission type with id = %d not found".formatted(id))
        );
    }

    @Cacheable(cacheNames = "merchantStatuses", key = "#id")
    public MerchantStatusEntity getMerchantStatus(Long id) {
        return merchantStatusRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Merchant status with id = %d not found".formatted(id))
        );
    }

}
