package com.example.miniacquiring.service;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetCommissionResponse;
import com.example.miniacquiring.core.enums.CommissionType;
import com.example.miniacquiring.core.enums.OperationStatus;
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
        var paidOperations = operationStorage.findByStatus(OperationStatus.PAID);
        if (paidOperations.isEmpty()) {
            log.info("No paid operations found. Nothing to process.");
            return;
        }
        createCommissions(paidOperations);
        updateOperations(paidOperations);
    }

    public GetCommissionResponse getById(Long id) {
        try {
            log.info("Trying to find commission with id = {}", id);
            var commission = commissionStorage.getById(id);
            return dtoMapper.toResponse(commission);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    private void createCommissions(List<OperationEntity> paidOperations) {
        var commissions = paidOperations.stream().map(this::getCommissionForOperation).toList();
        commissionStorage.saveAll(commissions);
    }

    private CommissionEntity getCommissionForOperation(OperationEntity operation) {
        try {
            var merchant = operation.getMerchant();
            var type = merchant.getCommissionType().getCode();
            var strategy = switch (type) {
                case CommissionType.PERCENTAGE -> percentageCommissionStrategy;
                case CommissionType.FIXED -> fixedCommissionStrategy;
            };
            BigDecimal totalCommission = strategy.calculate(operation.getSum(), merchant.getCommissionValue());
            return createCommissionEntity(operation, totalCommission);
        } catch (IllegalStateException exception) {
            log.error(exception.getMessage());
            throw exception;
        }
    }

    private CommissionEntity createCommissionEntity(OperationEntity operation, BigDecimal totalCommission) {
        return CommissionEntity
                .builder()
                .operation(operation)
                .totalCommission(totalCommission)
                .processedAt(LocalDateTime.now())
                .build();
    }

    private void updateOperations(List<OperationEntity> paidOperations) {
        var updatedOperations = updateOperationStatuses(paidOperations);
        operationStorage.saveAll(updatedOperations);
    }

    private List<OperationEntity> updateOperationStatuses(List<OperationEntity> paidOperations) {
        try {
            var completedStatus = operationStorage.findOperationStatus(OperationStatus.COMPLETED);
            return paidOperations.stream()
                    .map(operation ->
                            operation.toBuilder().status(completedStatus).build())
                    .toList();
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
