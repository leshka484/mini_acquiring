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

    public void deleteById(List<Long> ids) {
        List<Long> uniqueIds = ids.stream()
                .distinct()
                .toList();
        if (merchantRepository.existsAllById(uniqueIds)) {
            throw new IllegalArgumentException("Some merchants do not exist");
        }
        merchantRepository.deleteAllById(uniqueIds);
    }

    public void deleteById(Long id) {
        if (merchantRepository.existsById(id)) {
            throw new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id));
        }
        merchantRepository.deleteById(id);
    }

    public Page<MerchantEntity> findAll(Pageable pageable) {
        return merchantRepository.findAll(pageable);
    }

    public MerchantEntity findById(Long id) {
        return merchantRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id))
        );
    }

    public MerchantEntity findByName(String name) {
        return merchantRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Merchant with name '%s' not found".formatted(name))
        );
    }

    public Long save(MerchantEntity merchant) {
        merchantRepository.save(merchant);
        return merchant.getId();
    }

}
