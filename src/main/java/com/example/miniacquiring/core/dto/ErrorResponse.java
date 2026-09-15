package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        @JsonProperty("status")
        int status,

        @JsonProperty("message")
        String message) {

}
