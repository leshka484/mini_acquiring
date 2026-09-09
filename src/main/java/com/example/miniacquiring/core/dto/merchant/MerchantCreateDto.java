package com.example.miniacquiring.core.dto.merchant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantCreateDto {

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0")
    private BigDecimal commissionValue;

    @NotNull
    private Long commissionTypeId;

}
