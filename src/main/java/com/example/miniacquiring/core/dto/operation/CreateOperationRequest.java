package com.example.miniacquiring.core.dto.operation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateOperationRequest(

        @NotNull
        Long merchantId,

        @NotNull
        @Min(1)
        Long statusId,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal sum,

        @NotNull
        @Min(0)
        Long typeId,

        @Min(0)
        Long parentId,

        @PastOrPresent
        LocalDateTime createdAt,

        @PastOrPresent
        LocalDateTime processedAt) {

}
