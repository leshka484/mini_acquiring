package com.example.miniacquiring.service.commisstionStrategy;

import com.example.miniacquiring.core.enums.CommissionType;
import com.example.miniacquiring.core.utils.MoneyUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class PercentageCommissionStrategy implements CommissionStrategy {

    @Override
    public CommissionType getCommissionType() {
        return CommissionType.PERCENTAGE;
    }

    public Long calculate(Long operationSum, BigDecimal commissionValue) {
        var commission = MoneyUtils.convert(operationSum)
                .multiply(commissionValue)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.UP);
        return MoneyUtils.convert(commission);
    }

}
