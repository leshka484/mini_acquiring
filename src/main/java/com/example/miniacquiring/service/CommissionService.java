package com.example.miniacquiring.service;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetCommissionResponse;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.service.commisstionStrategy.CommissionStrategy;
import com.example.miniacquiring.service.commisstionStrategy.FixedCommissionStrategy;
import com.example.miniacquiring.service.commisstionStrategy.PercentageCommissionStrategy;
import com.example.miniacquiring.storage.CommissionStorage;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import java.math.BigDecimal;
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
    private final DtoMapper dtoMapper;
    PercentageCommissionStrategy percentageCommissionStrategy;
    FixedCommissionStrategy fixedCommissionStrategy;

    public void processCommissions() {
        var paidOperations = operationStorage.findPaid(Const.PAID_OPERATION_STATUS);
        paidOperations.stream().map(operation -> {
            var merchant = operation.getMerchant();
            var type = merchant.getCommissionType().getType();
            CommissionStrategy strategy = switch (type) {
                case Const.PERCENTAGE_COMMISSION -> percentageCommissionStrategy;
                case Const.FIXED_COMMISSION -> fixedCommissionStrategy;
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
            BigDecimal totalCommission = strategy.calculate(operation.getSum(), merchant.getCommissionValue());
            return createCommissionEntity(operation, totalCommission);
        }).forEach(commissionStorage::save);

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

    public void deleteById(Long id) {
        log.info("Deleting commission with id = {}", id);
        commissionStorage.deleteById(id);
    }

    private CommissionEntity createCommissionEntity(OperationEntity operation, BigDecimal totalCommission) {
        return CommissionEntity
                .builder()
                .operation(operation)
                .totalCommission(totalCommission)
                .build();
    }

}
