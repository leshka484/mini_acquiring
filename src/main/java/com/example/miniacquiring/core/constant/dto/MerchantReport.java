package com.example.miniacquiring.core.constant.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MerchantReport {

    private Long merchantId;
    private String merchantName;
    private Long operationsCount;
    private BigDecimal sumOperations;
    private Long commissionsCount;
    private BigDecimal sumCommissions;

}