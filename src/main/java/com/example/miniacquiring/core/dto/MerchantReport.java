package com.example.miniacquiring.core.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record MerchantReport(

        Long merchantId,
        String merchantName,
        Long operationsCount,
        BigDecimal sumOperations,
        Long commissionsCount,
        BigDecimal sumCommissions) {

}