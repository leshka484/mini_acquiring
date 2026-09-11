package com.example.miniacquiring.core.dto.operation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

public record UpdateOperationRequest(

        @NotNull
        @Min(1)
        Long statusId,

        @NotNull
        @Min(1)
        Long typeId,

        @Min(1)
        Long parentId,

        @PastOrPresent
        LocalDateTime processedAt) {

}
