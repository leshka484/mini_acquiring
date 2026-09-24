package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.dto.DeleteOperationRequest;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.UpsertOperationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OperationControllerDoc {

    @Operation(summary = "Create operation")
    @ApiResponse(responseCode = "200", description = "Operation created successfully")
    @PostMapping
    public void create(@Valid @RequestBody UpsertOperationRequest request);

    @Operation(summary = "Get operation by id")
    @ApiResponse(responseCode = "200", description = "Operation found")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @GetMapping("/{id}")
    public GetOperationResponse getById(Long id);

    @Operation(summary = "Get all operations")
    @ApiResponse(responseCode = "200", description = "Operations found")
    @ApiResponse(responseCode = "404", description = "One or more operations not found")
    @GetMapping
    public Page<GetOperationResponse> getAll(@PageableDefault(size = 20) Pageable pageable);

    @Operation(summary = "Operation payment")
    @ApiResponse(responseCode = "200", description = "Operation paid")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @PostMapping("/{id}/payment")
    public void update(Long id, @Valid @RequestBody UpsertOperationRequest request);

    @Operation(summary = "Operation cancellation")
    @ApiResponse(responseCode = "200", description = "Operation cancelled")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @PostMapping("/{id}/cancellation")
    public void cancellation(Long id);

    @Operation(summary = "Delete many operations by their ids")
    @ApiResponse(responseCode = "204", description = "Operations successfully deleted")
    @DeleteMapping
    public void deleteById(@Valid @RequestBody DeleteOperationRequest request);

}
