package com.example.miniacquiring.core.dto.merchant;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateMerchantRequest(

        @NotBlank
        @JsonProperty("name")
        String name, //TODO: Для всех полей указываем @JsonProperty

        @NotNull
        @DecimalMin("0.01")
        BigDecimal commissionValue,

        @NotNull
        Long commissionTypeId) {

}
