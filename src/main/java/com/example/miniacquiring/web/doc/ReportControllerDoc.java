package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.dto.GetMerchantReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;

public interface ReportControllerDoc {

    @Operation(summary = "Get commission by id")
    @ApiResponse(responseCode = "200", description = "Full report by merchant")
    @ApiResponse(responseCode = "404", description = "Merchant not found")
    @GetMapping("merchant/{id}/full-report")
    public GetMerchantReportResponse fullReport(Long id);

    @Operation(summary = "Get commission by id")
    @ApiResponse(responseCode = "200", description = "Report by time for merchant")
    @ApiResponse(responseCode = "404", description = "Merchant not found")
    @GetMapping("merchant/{id}/time-report")
    public GetMerchantReportResponse reportByTime(Long id, CreateMerchantReportRequest request);

}
