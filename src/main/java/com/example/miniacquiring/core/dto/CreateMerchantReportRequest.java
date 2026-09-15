package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

public record CreateMerchantReportRequest(

        @NotNull
        @PastOrPresent
        @JsonProperty("start_date_time")
        LocalDateTime startDateTime,

        @NotNull
        @PastOrPresent
        @JsonProperty("end_date_time")
        LocalDateTime endDateTime) {

}