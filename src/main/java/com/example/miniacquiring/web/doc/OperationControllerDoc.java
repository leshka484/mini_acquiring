package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.PayRequest;
import com.example.miniacquiring.core.dto.UpsertOperationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OperationControllerDoc {

    @Operation(summary = "Create operation")
    @ApiResponse(responseCode = "200", description = "Operation created successfully")
    @PostMapping
    void create(@Valid @RequestBody UpsertOperationRequest request);

    @Operation(summary = "Get operation by id")
    @ApiResponse(responseCode = "200", description = "Operation found")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @GetMapping("/{id}")
    GetOperationResponse getById(Long id);

    @Operation(summary = "Get all operations")
    @ApiResponse(responseCode = "200", description = "Operations found")
    @ApiResponse(responseCode = "404", description = "One or more operations not found")
    @GetMapping
    Page<GetOperationResponse> getAll(@PageableDefault(size = 20) Pageable pageable);

    @Operation(summary = "Operation payment")
    @ApiResponse(responseCode = "200", description = "Operation paid")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @PutMapping("/payment")
    void payment(@Valid @RequestBody PayRequest request);

    @Operation(summary = "Operation cancellation")
    @ApiResponse(responseCode = "200", description = "Operation cancelled")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @PutMapping("/{id}/cancellation")
    void cancellation(@PathVariable Long id);

}
