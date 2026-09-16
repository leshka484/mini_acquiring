package com.example.miniacquiring.web;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.dto.DeleteMerchantRequest;
import com.example.miniacquiring.core.dto.GetMerchantResponse;
import com.example.miniacquiring.core.dto.MerchantFilter;
import com.example.miniacquiring.core.dto.UpsertMerchantRequest;
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
    public void create(@Valid @RequestBody UpsertMerchantRequest request) {
        merchantService.create(request);
    }

    @GetMapping()
    public Page<GetMerchantResponse> getFilteredMerchants(
            @ParameterObject
            @PageableDefault(size = Const.PER_PAGE) Pageable pageable,
            MerchantFilter filter) {
        return merchantService.getFilteredMerchants(filter, pageable);
    }

    @PutMapping("/{id}")
    public void update(Long id, @Valid @RequestBody UpsertMerchantRequest request) {
        merchantService.update(id, request);
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
