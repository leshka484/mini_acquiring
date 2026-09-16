package com.example.miniacquiring.service.commisstionStrategy;

import com.example.miniacquiring.core.Const;
import java.math.BigDecimal;

public class FixedCommissionStrategy implements CommissionStrategy {

    public String getType() {
        return Const.FIXED_COMMISSION;
    }

    public BigDecimal calculate(BigDecimal operationSum, BigDecimal commissionValue) {
        return commissionValue;
    }

}
