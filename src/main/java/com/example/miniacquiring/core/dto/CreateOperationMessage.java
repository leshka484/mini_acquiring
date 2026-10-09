package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOperationMessage(

        @NotNull
        @JsonProperty("public_merchant_id")
        UUID publicMerchantId,

        @NotNull
        @Min(1)
        @JsonProperty("sum")
        Long sum) {

}
