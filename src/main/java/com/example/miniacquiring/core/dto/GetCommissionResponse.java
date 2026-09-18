package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record GetCommissionResponse(

        @JsonProperty("id")
        Long id,

        @JsonProperty("operation_id")
        Long operation,

        @JsonProperty("total_commission")
        BigDecimal totalCommission) {

}
