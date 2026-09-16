package com.example.miniacquiring.service.commisstionStrategy;

import java.math.BigDecimal;

public interface CommissionStrategy {

    String getType();

    BigDecimal calculate(BigDecimal operationSum, BigDecimal commissionValue);

}
