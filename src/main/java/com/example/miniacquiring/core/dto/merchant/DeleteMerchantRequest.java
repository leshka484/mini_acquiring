package com.example.miniacquiring.core.dto.merchant;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record DeleteMerchantRequest(

        @NotEmpty
        List<Long> ids) {

}
