package com.example.miniacquiring.service;

import com.example.miniacquiring.core.constant.dto.MerchantReport;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.repository.CommissionRepository;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import com.example.miniacquiring.storage.repository.MerchantRepository;
import com.example.miniacquiring.storage.repository.OperationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MerchantService {
    // CRUD и отчеты

    private final MerchantRepository merchantRepository;
    private final CommissionTypeRepository commissionTypeRepository;
    private final OperationRepository operationRepository;
    private final CommissionRepository commissionRepository;

    public MerchantEntity create(String name, BigDecimal commissionValue, String commissionType) {
        if (merchantRepository.existsByName(name)) {
            throw new IllegalArgumentException("Merchant with this name already exists");
        }

        CommissionTypeEntity type = commissionTypeRepository.findByType(commissionType).orElseThrow(
                () -> new IllegalArgumentException("Commission type not found")
        );

        MerchantEntity merchant = MerchantEntity
                .builder()
                .name(name)
                .commissionValue(commissionValue)
                .commissionType(type)
                .build();

        return merchantRepository.save(merchant);

    }

    public MerchantEntity readById(Long id) {
        return merchantRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id))
        );

    }

    public MerchantEntity readByName(String name) {
        return merchantRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Merchant with name '%s' not found".formatted(name))
        );
    }

    public void update(Long id, String name, BigDecimal commissionValue, CommissionTypeEntity commissionType) {

        if (!merchantRepository.existsById(id)) {
            throw new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id));
        }

        MerchantEntity merchant = MerchantEntity
                .builder()
                .id(id)
                .name(name)
                .commissionValue(commissionValue)
                .commissionType(commissionType)
                .build();

        merchantRepository.save(merchant);
    }

    public void deleteById(Long id) {

        if (!merchantRepository.existsById(id)) {
            throw new IllegalArgumentException("Merchant with id = %d does not exist".formatted(id));
        }
        merchantRepository.deleteById(id);
    }

    public void deleteByName(String name) {
        if (!merchantRepository.existsByName(name)) {
            throw new IllegalArgumentException("Merchant with id = %s does not exist".formatted(name));
        }
        merchantRepository.deleteByName(name);
    }

    private MerchantReport buildReport(
            MerchantEntity merchant,
            Long operationsCount,
            BigDecimal sumOperations,
            Long commissionsCount,
            BigDecimal sumCommissions
    ) {
        return MerchantReport.builder()
                .merchantId(merchant.getId())
                .merchantName(merchant.getName())
                .operationsCount(operationsCount)
                .sumOperations(sumOperations)
                .commissionsCount(commissionsCount)
                .sumCommissions(sumCommissions)
                .build();
    }

    public MerchantReport getFullReport(Long merchantId) {
        MerchantEntity merchant = readById(merchantId);

        Long operationsCount = operationRepository.countMerchantOperations(merchantId);
        Long commissionsCount = commissionRepository.countMerchantCommissions(merchantId);

        BigDecimal sumOperations = operationRepository.sumMerchantOperations(merchantId);
        BigDecimal sumCommissions = commissionRepository.sumMerchantCommissions(merchantId);

        return buildReport(merchant, operationsCount, sumOperations, commissionsCount, sumCommissions);
    }

    public MerchantReport getReportByTime(Long merchantId, LocalDateTime from, LocalDateTime to) {
        MerchantEntity merchant = readById(merchantId);

        Long operationsCount = operationRepository.countMerchantOperationsBetween(merchantId, from, to);
        Long commissionsCount = commissionRepository.countMerchantCommissionsBetween(merchantId, from, to);

        BigDecimal sumOperations = operationRepository.sumMerchantOperationsBetween(merchantId, from, to);
        BigDecimal sumCommissions = commissionRepository.sumMerchantCommissionsBetween(merchantId, from, to);

        return buildReport(merchant, operationsCount, sumOperations, commissionsCount, sumCommissions);

    }

}