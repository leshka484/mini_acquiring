package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommissionTypeStorage {

    private final CommissionTypeRepository commissionTypeRepository;

    public CommissionTypeEntity findById(Long id) {
        return commissionTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Commission type not found")
        );
    }

}
