package com.example.miniacquiring.core.dto.merchant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateMerchantRequest(

        @NotBlank
        String name,

        @NotNull
        @DecimalMin("0")
        BigDecimal commissionValue,

        @NotNull
        Long commissionTypeId) {

}
