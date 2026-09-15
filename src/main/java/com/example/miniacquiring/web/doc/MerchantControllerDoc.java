package com.example.miniacquiring.web.doc;

import com.example.miniacquiring.core.dto.DeleteMerchantRequest;
import com.example.miniacquiring.core.dto.GetMerchantResponse;
import com.example.miniacquiring.core.dto.MerchantFilter;
import com.example.miniacquiring.core.dto.MerchantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
    public void create(@Valid @RequestBody MerchantRequest request);

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
    public void update(Long id, @Valid @RequestBody MerchantRequest request);

    @Operation(summary = "Delete many merchants by their ids")
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
