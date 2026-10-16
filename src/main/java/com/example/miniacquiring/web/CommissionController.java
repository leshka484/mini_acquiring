package com.example.miniacquiring.web;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.dto.GetCommissionResponse;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.service.CommissionService;
import com.example.miniacquiring.web.doc.CommissionControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public Page<GetCommissionResponse> getAll(
            @ParameterObject
            @PageableDefault(size = Const.PER_PAGE) Pageable pageable) {
        return commissionService.getAll(pageable);
    }

}
