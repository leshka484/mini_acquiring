package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record GetMerchantResponse(

        @JsonProperty("id")
        Long id,

        @JsonProperty("name")
        String name,

        @JsonProperty("commission_type")
        String commissionType,

        @JsonProperty("commission_value")
        BigDecimal commissionValue,

        @JsonProperty("status")
        String status) {

}
