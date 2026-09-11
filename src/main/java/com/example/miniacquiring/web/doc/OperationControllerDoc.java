package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.dto.operation.CreateOperationRequest;
import com.example.miniacquiring.core.dto.operation.DeleteOperationRequest;
import com.example.miniacquiring.core.dto.operation.GetOperationResponse;
import com.example.miniacquiring.core.dto.operation.UpdateOperationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OperationControllerDoc {

    @Operation(summary = "Create operation")
    @ApiResponse(responseCode = "200", description = "Operation created successfully")
    @PostMapping
    public GetOperationResponse create(@Valid @RequestBody CreateOperationRequest request);

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

    @Operation(summary = "Update operation")
    @ApiResponse(responseCode = "200", description = "Operation updated")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @PutMapping("/{id}")
    public GetOperationResponse update(Long id, @Valid @RequestBody UpdateOperationRequest request);

    @Operation(summary = "Delete many operations by their ids")
    @ApiResponse(responseCode = "204", description = "Operations successfully deleted")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @DeleteMapping
    public void delete(@Valid @RequestBody DeleteOperationRequest request);

    @Operation(summary = "Delete operation by id")
    @ApiResponse(responseCode = "204", description = "Operation successfully deleted")
    @ApiResponse(responseCode = "404", description = "Operation not found")
    @DeleteMapping("/{id}")
    public void delete(Long id);

}
