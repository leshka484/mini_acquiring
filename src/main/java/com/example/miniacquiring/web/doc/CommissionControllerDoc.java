package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.dto.GetCommissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

public interface CommissionControllerDoc {

    @Operation(summary = "Get commission by id")
    @ApiResponse(responseCode = "200", description = "Commission found")
    @ApiResponse(responseCode = "404", description = "Commission not found")
    @GetMapping("/{id}")
    GetCommissionResponse getById(Long id);

}
