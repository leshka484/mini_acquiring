package com.example.miniacquiring.core.dto;

import java.time.LocalDateTime;

public record CreateMerchantReportRequest(
        LocalDateTime from,
        LocalDateTime to) {

}