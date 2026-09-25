package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PayRequest(

        @NotNull
        @JsonProperty("operation_id")
        Long operationId,

        @NotNull
        @JsonProperty("sum")
        BigDecimal sum) {

}

