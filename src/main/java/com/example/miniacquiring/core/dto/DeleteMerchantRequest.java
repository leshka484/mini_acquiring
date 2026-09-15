package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record DeleteMerchantRequest(

        @NotEmpty
        @JsonProperty("ids")
        List<Long> ids) {

}
