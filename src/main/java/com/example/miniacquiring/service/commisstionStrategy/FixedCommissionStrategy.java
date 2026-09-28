package com.example.miniacquiring.service.commisstionStrategy;

import com.example.miniacquiring.core.enums.CommissionType;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class FixedCommissionStrategy implements CommissionStrategy {

    @Override
    public CommissionType getCommissionType() {
        return CommissionType.FIXED;
    }

    public Long calculate(Long operationSum, BigDecimal commissionValue) {
        return commissionValue.movePointRight(2).longValueExact();
    }

}
