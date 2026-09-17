package com.example.miniacquiring.service.commisstionStrategy;

import java.math.BigDecimal;

public interface CommissionStrategy {

    BigDecimal calculate(BigDecimal operationSum, BigDecimal commissionValue);

}
