package com.example.miniacquiring.web;

import com.example.miniacquiring.core.dto.merchant.CreateMerchantRequest;
import com.example.miniacquiring.core.dto.merchant.DeleteMerchantRequest;
import com.example.miniacquiring.core.dto.merchant.GetMerchantResponse;
import com.example.miniacquiring.core.dto.merchant.UpdateMerchantRequest;
import com.example.miniacquiring.service.MerchantService;
import com.example.miniacquiring.web.doc.MerchantControllerDoc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
public class MerchantController implements MerchantControllerDoc {

    private final MerchantService merchantService;

    @PostMapping
    public GetMerchantResponse create(@Valid @RequestBody CreateMerchantRequest request) {
        return merchantService.create(request);
    }

    @GetMapping("/{id}")
    public GetMerchantResponse getById(Long id) {
        return merchantService.getById(id);
    }

    @GetMapping
    public Page<GetMerchantResponse> getAll(
            @ParameterObject
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return merchantService.getAll(pageable);
    }

    @GetMapping("/by-name/{name}")
    public GetMerchantResponse getByName(String name) {
        return merchantService.getByName(name);
    }

    @PutMapping("/{id}")
    public GetMerchantResponse update(Long id, @Valid @RequestBody UpdateMerchantRequest request) {
        return merchantService.update(id, request);
    }

    @DeleteMapping
    public void delete(@Valid @RequestBody DeleteMerchantRequest request) {
        merchantService.deleteById(request.ids());
    }

    @DeleteMapping("/{id}")
    public void delete(Long id) {
        merchantService.deleteById(id);
    }

}
