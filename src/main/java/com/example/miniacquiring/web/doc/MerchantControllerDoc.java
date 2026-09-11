package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.dto.merchant.CreateMerchantRequest;
import com.example.miniacquiring.core.dto.merchant.DeleteMerchantRequest;
import com.example.miniacquiring.core.dto.merchant.GetMerchantResponse;
import com.example.miniacquiring.core.dto.merchant.UpdateMerchantRequest;
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

public interface MerchantControllerDoc {

    @Operation(summary = "Create merchant")
    @ApiResponse(responseCode = "200", description = "Merchant created successfully")
    @PostMapping
    public GetMerchantResponse create(@Valid @RequestBody CreateMerchantRequest request);

    @Operation(summary = "Get merchant by id")
    @ApiResponse(responseCode = "200", description = "Merchant found")
    @ApiResponse(responseCode = "404", description = "Merchant not found")
    @GetMapping("/{id}")
    public GetMerchantResponse getById(Long id);

    @Operation(summary = "Get all merchants")
    @ApiResponse(responseCode = "200", description = "Merchants found")
    @ApiResponse(responseCode = "404", description = "One or more merchants not found")
    @GetMapping
    public Page<GetMerchantResponse> getAll(@PageableDefault(size = 20) Pageable pageable);

    @Operation(summary = "Get merchant by name")
    @ApiResponse(responseCode = "200", description = "Merchant found")
    @ApiResponse(responseCode = "404", description = "Merchant not found")
    @GetMapping("/{name}")
    public GetMerchantResponse getByName(String name);

    @Operation(summary = "Update merchant")
    @ApiResponse(responseCode = "200", description = "Merchant updated")
    @ApiResponse(responseCode = "404", description = "Merchant not found")
    @PutMapping("/{id}")
    public GetMerchantResponse update(Long id, @Valid @RequestBody UpdateMerchantRequest request);

    @Operation (summary = "Delete many merchants by their ids")
    @ApiResponse(responseCode = "204", description = "Merchants successfully deleted")
    @ApiResponse(responseCode = "404", description = "Merchants not found")
    @DeleteMapping
    public void delete(@Valid @RequestBody DeleteMerchantRequest request);

    @Operation(summary = "Delete merchant by id")
    @ApiResponse(responseCode = "204", description = "Merchant successfully deleted")
    @ApiResponse(responseCode = "404", description = "Merchant not found")
    @DeleteMapping("/{id}")
    public void delete(Long id);

}
