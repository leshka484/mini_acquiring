package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommissionTypeStorage {

    private final CommissionTypeRepository commissionTypeRepository;

    public CommissionTypeEntity findByType(String type) {
        return commissionTypeRepository.findByType(type).orElseThrow(
                () -> new IllegalArgumentException("Commission type not found")
        );
    }

    public CommissionTypeEntity findById(Long id) {
        return commissionTypeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Commission type not found")
        );
    }

}
