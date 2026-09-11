package com.example.miniacquiring.core.dto.merchant;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record GetMerchantResponse(

        Long id,
        String name,
        String commissionType,
        BigDecimal commissionValue) {

}
