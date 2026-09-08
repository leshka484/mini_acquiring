package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommissionTypeStorage {

    private final CommissionTypeRepository commissionTypeRepository;

    public CommissionTypeEntity findByType(String type) {
        return commissionTypeRepository.findByType(type).orElseThrow(
                () -> new IllegalArgumentException("Commission type not found")
        );
    }

}
