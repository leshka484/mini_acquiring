package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetReferenceEntityResponse(

        @JsonProperty("id")
        Long id,

        @JsonProperty("code")
        String code,

        @JsonProperty("name")
        String name) {

}
