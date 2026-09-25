package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.dto.MerchantFilter;
import com.example.miniacquiring.core.enums.MerchantStatus;
import com.example.miniacquiring.core.exception.EntityNotActiveException;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.repository.MerchantRepository;
import com.example.miniacquiring.storage.repository.specification.MerchantSpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MerchantStorage {

    private final MerchantRepository merchantRepository;

    public void deleteById(List<Long> ids) {
        merchantRepository.deleteAllById(ids);
    }

    public Page<MerchantEntity> getFilteredMerchants(MerchantFilter filter, Pageable pageable) {
        return merchantRepository.findAll(MerchantSpecification.filter(filter), pageable);
    }

    public MerchantEntity findById(Long id) {
        return merchantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Merchant with id = %d does not exist".formatted(id))
        );
    }

    public void isActive(Long id, MerchantStatus status) {
        if (!merchantRepository.isActive(id, status)) {
            throw new EntityNotActiveException("Merchant with id = %d is not active".formatted(id));
        }
    }

    public Long save(MerchantEntity merchant) {
        merchantRepository.save(merchant);
        return merchant.getId();
    }

}
