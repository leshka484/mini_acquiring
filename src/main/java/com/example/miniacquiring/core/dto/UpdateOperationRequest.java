package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

public record UpdateOperationRequest(

        @NotNull
        @Min(1)
        @JsonProperty("status_id")
        Long statusId,

        @NotNull
        @Min(1)
        @JsonProperty("type_id")
        Long typeId,

        @Min(1)
        @JsonProperty("parent_id")
        Long parentId,

        @PastOrPresent
        @JsonProperty("processed_at")
        LocalDateTime processedAt) {

}
