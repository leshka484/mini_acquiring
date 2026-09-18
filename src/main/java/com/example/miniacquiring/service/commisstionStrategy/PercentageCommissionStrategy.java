package com.example.miniacquiring.service.commisstionStrategy;

import com.example.miniacquiring.core.Const;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class PercentageCommissionStrategy implements CommissionStrategy {

    public BigDecimal calculate(BigDecimal operationSum, BigDecimal commissionValue) {
        return operationSum
                .multiply(commissionValue)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.UP);
    }

}
