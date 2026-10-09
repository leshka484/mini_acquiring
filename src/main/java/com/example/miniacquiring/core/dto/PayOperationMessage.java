package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PayOperationMessage(

        @NotNull
        @JsonProperty("public_operation_id")
        UUID publicOperationId,

        @NotNull
        @JsonProperty("sum")
        Long sum) {

}
