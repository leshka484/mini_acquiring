package com.example.miniacquiring.core.dto.merchant;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantDeleteDto {

    @NotEmpty
    private List<@Positive Long> ids;

}
