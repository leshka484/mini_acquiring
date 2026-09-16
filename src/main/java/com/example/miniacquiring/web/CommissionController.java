package com.example.miniacquiring.web;

import com.example.miniacquiring.core.dto.GetCommissionResponse;
import com.example.miniacquiring.service.CommissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commissions")
@RequiredArgsConstructor
public class CommissionController {

    private final CommissionService commissionService;

    @PostMapping

    @GetMapping("/{id}")
    public GetCommissionResponse getById(Long id) {
        return commissionService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(Long id) {
        commissionService.deleteById(id);
    }

    @DeleteMapping
    public void deleteById(List<Long> ids) {
        commissionService.deleteById(ids);
    }


}
