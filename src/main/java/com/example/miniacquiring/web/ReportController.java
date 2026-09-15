package com.example.miniacquiring.web;

import com.example.miniacquiring.core.dto.CreateMerchantReportRequest;
import com.example.miniacquiring.core.dto.GetMerchantReportResponse;
import com.example.miniacquiring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("merchant/{id}/full-report")
    public GetMerchantReportResponse fullReport(Long id) {
        return reportService.getMerchantFullReport(id);
    }

    @GetMapping("merchant/{id}/time-report")
    public GetMerchantReportResponse reportByTime(Long id, CreateMerchantReportRequest request) {
        return reportService.getMerchantReportByTime(id, request);
    }

}
