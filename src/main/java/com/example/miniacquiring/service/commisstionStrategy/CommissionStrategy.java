package com.example.miniacquiring.service.commisstionStrategy;

import com.example.miniacquiring.core.enums.CommissionType;
import java.math.BigDecimal;

public interface CommissionStrategy {

    CommissionType getCommissionType();

    Long calculate(Long operationSum, BigDecimal commissionValue);

}
