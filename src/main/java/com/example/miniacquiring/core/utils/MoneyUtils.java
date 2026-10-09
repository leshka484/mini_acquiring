package com.example.miniacquiring.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtils {

    private static final BigDecimal KOPECKS_IN_RUBLE = new BigDecimal("100.00");

    private MoneyUtils() {

    }

    public static BigDecimal convert(Long kopecks) {
        return BigDecimal.valueOf(kopecks)
                .divide(KOPECKS_IN_RUBLE, 2, RoundingMode.UNNECESSARY);
    }

    public static Long convert(BigDecimal sum) {
        return sum
                .multiply(KOPECKS_IN_RUBLE)
                .longValueExact();
    }

}
