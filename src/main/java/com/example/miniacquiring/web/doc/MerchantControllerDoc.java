package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.dto.DeleteMerchantRequest;
import com.example.miniacquiring.core.dto.GetMerchantResponse;
import com.example.miniacquiring.core.dto.MerchantFilter;
import com.example.miniacquiring.core.dto.UpsertMerchantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MerchantControllerDoc {

    @Operation(summary = "Create merchant")
    @ApiResponse(responseCode = "200", description = "Merchant created successfully")
    @PostMapping
    public void create(@Valid @RequestBody UpsertMerchantRequest request);

    @Operation(summary = "Filter merchants")
    @ApiResponse(responseCode = "200", description = "Merchants found with filters")
    @GetMapping
    public Page<GetMerchantResponse> getFilteredMerchants(
            @ParameterObject
            @PageableDefault(size = 20, sort = "name") Pageable pageable,
            MerchantFilter filter);

    @Operation(summary = "Update merchant")
    @ApiResponse(responseCode = "200", description = "Merchant updated")
    @ApiResponse(responseCode = "404", description = "Merchant not found")
    @PutMapping("/{id}")
    public void update(Long id, @Valid @RequestBody UpsertMerchantRequest request);

    @Operation(summary = "Delete many merchants by their ids")
    @ApiResponse(responseCode = "204", description = "Merchants successfully deleted")
    @DeleteMapping
    public void deleteById(@Valid @RequestBody DeleteMerchantRequest request);

}
