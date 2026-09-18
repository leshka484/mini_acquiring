package com.example.miniacquiring.service;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetCommissionResponse;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.service.commisstionStrategy.FixedCommissionStrategy;
import com.example.miniacquiring.service.commisstionStrategy.PercentageCommissionStrategy;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommissionService {

    private final CommissionStorage commissionStorage;
    private final OperationStorage operationStorage;
    private final DtoMapper dtoMapper;
    private final PercentageCommissionStrategy percentageCommissionStrategy;
    private final FixedCommissionStrategy fixedCommissionStrategy;

    @Transactional
    @Scheduled(cron = "${commission.scheduler.cron}")
    public void processCommissions() {
        log.info("Processing commissions");
        var paidOperations = operationStorage.findPaid();
        if (paidOperations.isEmpty()) {
            log.info("No paid operations found. Nothing to process.");
            return;
        }
        processPaidOperations(paidOperations);
    }

    public GetCommissionResponse getById(Long id) {
        try {
            log.info("Trying to find commission with id = {}", id);
            return dtoMapper.toResponse(commissionStorage.getById(id));
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }

    }

    public void deleteById(List<Long> ids) {
        log.info("Deleting commissions");
        commissionStorage.deleteById(ids);
    }

    private void processPaidOperations(List<OperationEntity> paidOperations) {
        var commissions = paidOperations.stream().map(this::getCommissionForOperation).toList();
        commissionStorage.saveAll(commissions);
        operationStorage.completeAllPaid();
    }

    private CommissionEntity createCommissionEntity(OperationEntity operation, BigDecimal totalCommission) {
        return CommissionEntity
                .builder()
                .operation(operation)
                .totalCommission(totalCommission)
                .processedAt(LocalDateTime.now())
                .build();
    }

    private CommissionEntity getCommissionForOperation(OperationEntity operation) {
        try {
            var merchant = operation.getMerchant();
            var type = merchant.getCommissionType().getType();
            var strategy = switch (type) {
                case Const.PERCENTAGE_COMMISSION -> percentageCommissionStrategy;
                case Const.FIXED_COMMISSION -> fixedCommissionStrategy;
                default -> throw new IllegalStateException("No strategy for commission type" + type);
            };
            BigDecimal totalCommission = strategy.calculate(operation.getSum(), merchant.getCommissionValue());
            return createCommissionEntity(operation, totalCommission);
        } catch (IllegalStateException exception) {
            log.error(exception.getMessage());
            throw exception;
        }
    }

}
