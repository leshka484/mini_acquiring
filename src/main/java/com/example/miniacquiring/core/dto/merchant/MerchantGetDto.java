package com.example.miniacquiring.core.dto.merchant;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MerchantGetDto {

    private Long id;
    private String name;
    private String commissionType;
    private BigDecimal commissionValue;

}
