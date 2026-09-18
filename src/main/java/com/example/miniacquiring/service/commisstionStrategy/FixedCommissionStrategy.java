package com.example.miniacquiring.service.commisstionStrategy;

import com.example.miniacquiring.core.Const;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class FixedCommissionStrategy implements CommissionStrategy {

    public BigDecimal calculate(BigDecimal operationSum, BigDecimal commissionValue) {
        return commissionValue;
    }

}
