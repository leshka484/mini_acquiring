package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;

public class CommissionTypeStorage extends BaseStorage<CommissionTypeEntity, Long, CommissionTypeRepository> {

    public CommissionTypeStorage(CommissionTypeRepository commissionTypeRepository) {
        super(commissionTypeRepository);
    }

    public CommissionTypeEntity findByType(String type) {
        return repository.findByType(type).orElseThrow(
                () -> new IllegalArgumentException("Commission type not found")
        );
    }

    public CommissionTypeEntity findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Commission type not found")
        );
    }

}
