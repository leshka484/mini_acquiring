package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpsertOperationRequest(

        @NotNull
        @JsonProperty("merchant_id")
        Long merchantId,

        @NotNull
        @Min(1)
        @JsonProperty("status_id")
        Long statusId,

        @NotNull
        @DecimalMin("0.01")
        @JsonProperty("sum")
        BigDecimal sum,

        @NotNull
        @Min(0)
        @JsonProperty("type_id")
        Long typeId,

        @Min(0)
        @JsonProperty("parent_id")
        Long parentId,

        @PastOrPresent
        @JsonProperty("processed_at")
        LocalDateTime processedAt) {

}
