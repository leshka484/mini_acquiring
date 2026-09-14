package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateMerchantRequest(

        @NotBlank
        @JsonProperty("name")
        String name,

        @NotNull
        @DecimalMin("0.01")
        @JsonProperty("commission_value")
        BigDecimal commissionValue,

        @NotNull
        @JsonProperty("commission_type_id")
        Long commissionTypeId,

        @NotNull
        @JsonProperty("status_id")
        Long statusId) {

}
