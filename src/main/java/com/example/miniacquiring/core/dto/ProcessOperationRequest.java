package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ProcessOperationRequest(

        @NotNull
        @JsonProperty("operation_id")
        Long operationId,

        @NotNull
        @JsonProperty("sum")
        Long sum) {

}