package com.example.miniacquiring.web;

import com.example.miniacquiring.core.dto.GetCommissionResponse;
import com.example.miniacquiring.service.CommissionService;
import com.example.miniacquiring.web.doc.CommissionControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commissions")
@RequiredArgsConstructor
public class CommissionController implements CommissionControllerDoc {

    private final CommissionService commissionService;

    @GetMapping("/{id}")
    public GetCommissionResponse getById(@PathVariable Long id) {
        return commissionService.getById(id);
    }

}
