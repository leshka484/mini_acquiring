package com.example.miniacquiring.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.BindParam;

public record MerchantFilter(

        @JsonProperty("id")
        @BindParam("id")
        Long id,

        @JsonProperty("name")
        @BindParam("name")
        String name,

        @JsonProperty("commission_type_id")
        @BindParam("commission_type_id")
        Long commissionTypeId,

        @JsonProperty("status_id")
        @BindParam("status_id")
        Long statusId,

        @JsonProperty("min_commission_value")
        @BindParam("min_commission_value")
        BigDecimal minCommissionValue,

        @JsonProperty("max_commission_value")
        @BindParam("max_commission_value")
        BigDecimal maxCommissionValue) {

}
