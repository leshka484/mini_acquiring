package com.example.miniacquiring.storage;

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

    public void deleteById(List<Long> ids, String errorText) {
        List<Long> uniqueIds = ids.stream()
                .distinct()
                .toList();
        if (merchantRepository.existsAllById(uniqueIds)) {
            throw new IllegalArgumentException(errorText);
        }
        merchantRepository.deleteAllById(uniqueIds);
    }

    public void deleteById(Long id, String errorText) {
        if (merchantRepository.existsById(id)) {
            throw new IllegalArgumentException(errorText);
        }
        merchantRepository.deleteById(id);
    }

    public Page<MerchantEntity> getAll(Pageable pageable) {
        return merchantRepository.getAll(pageable);
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

    public Long save(MerchantEntity merchant) {
        merchantRepository.save(merchant);
        return merchant.getId();
    }

}
