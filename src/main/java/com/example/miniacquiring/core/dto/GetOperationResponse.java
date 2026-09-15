package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetOperationResponse(

        @JsonProperty("merchant")
        String merchant,

        @JsonProperty("status")
        String status,

        @JsonProperty("sum")
        BigDecimal sum,

        @JsonProperty("type")
        String type,

        @JsonProperty("parent_id")
        Long parentId,

        @JsonProperty("created_at")
        LocalDateTime createdAt,

        @JsonProperty("processed_at")
        LocalDateTime processedAt) {

}
