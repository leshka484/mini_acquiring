package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.dto.GetCommissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;

public interface CommissionControllerDoc {

    @Operation(summary = "Get commission by id")
    @ApiResponse(responseCode = "200", description = "Commission found")
    @ApiResponse(responseCode = "404", description = "Commission not found")
    @GetMapping("/{id}")
    GetCommissionResponse getById(Long id);

    @Operation(summary = "Get all commissions")
    @ApiResponse(responseCode = "200", description = "All commissions received")
    @GetMapping
    Page<GetCommissionResponse> getAll(
            @ParameterObject
            @PageableDefault(size = Const.PER_PAGE) Pageable pageable);

}
