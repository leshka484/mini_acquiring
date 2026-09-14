package com.example.miniacquiring.storage;

import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.MerchantNotActiveException;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.repository.MerchantRepository;
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

    public void deleteById(Long id) {
        merchantRepository.deleteById(id);
    }

    public Page<MerchantEntity> findAll(Pageable pageable) {
        return merchantRepository.findAll(pageable);
    }

    public MerchantEntity findById(Long id) {
        return merchantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Merchant with id = %d does not exist".formatted(id))
        );
    }

    public MerchantEntity findByName(String name) {
        return merchantRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Merchant with name '%s' not found".formatted(name))
        );
    }

    public void isActive(Long id) {
        if (!merchantRepository.isActive(id)) {
            throw new MerchantNotActiveException("Merchant with id = %d is not active".formatted(id));
        }
    }

    public Long save(MerchantEntity merchant) {
        merchantRepository.save(merchant);
        return merchant.getId();
    }

}
