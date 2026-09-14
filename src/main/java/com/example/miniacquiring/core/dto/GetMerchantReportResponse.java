package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record GetMerchantReportResponse(

        @JsonProperty("merchant_id")
        Long merchantId,

        @JsonProperty("merchant_name")
        String merchantName,

        @JsonProperty("operations_count")
        Long operationsCount,

        @JsonProperty("sum_operations")
        BigDecimal sumOperations,

        @JsonProperty("commissions_count")
        Long commissionsCount,

        @JsonProperty("sum_commissions")
        BigDecimal sumCommissions) {

}