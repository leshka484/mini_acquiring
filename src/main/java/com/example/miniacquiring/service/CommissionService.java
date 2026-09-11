package com.example.miniacquiring.service;

import com.example.miniacquiring.core.mapper.EntityMapper;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.MerchantStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommissionService {

    private final CommissionStorage commissionStorage;
    private final OperationStorage operationStorage;
    private final MerchantStorage merchantStorage;
    private final EntityMapper entityMapper;

    public void deleteById(List<Long> ids) {
        commissionStorage.deleteById(ids, "Some commissions do not exist");
        log.info("Commissions deleted");
    }

    public void deleteById(Long id) {
        commissionStorage.deleteById(id, "Commission with id = %d does not exist".formatted(id));
        log.info("Commission with id = {} deleted", id);
    }

    private CommissionEntity buildCommissionEntity(Long id, LocalDateTime processedAt) {
        return CommissionEntity
                .builder()
                .id(id)
                .processedAt(processedAt)
                .build();
    }

    private CommissionEntity buildCommissionEntity(OperationEntity operation,
                                                   BigDecimal totalCommission,
                                                   LocalDateTime processedAt) {
        return CommissionEntity
                .builder()
                .operation(operation)
                .totalCommission(totalCommission)
                .processedAt(processedAt)
                .build();
    }

}
