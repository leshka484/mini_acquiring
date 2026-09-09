package com.example.miniacquiring.web;

import com.example.miniacquiring.core.dto.merchant.MerchantCreateDto;
import com.example.miniacquiring.core.dto.merchant.MerchantDeleteDto;
import com.example.miniacquiring.core.dto.merchant.MerchantGetDto;
import com.example.miniacquiring.core.dto.merchant.MerchantUpdateDto;
import com.example.miniacquiring.service.MerchantService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping
    public MerchantGetDto create(@Valid @RequestBody MerchantCreateDto request) {
        return merchantService.create(request);
    }

    @GetMapping("/{id}")
    public MerchantGetDto getById(Long id) {
        return merchantService.getById(id);
    }

    @GetMapping
    public List<MerchantGetDto> getAll() {
        return merchantService.getAll();
    }

    @GetMapping("/{name}")
    public MerchantGetDto getByName(String name) {
        return merchantService.getByName(name);
    }

    @PutMapping("/{id}")
    public MerchantGetDto update(Long id, @Valid @RequestBody MerchantUpdateDto request) {
        return merchantService.update(id, request);
    }

    @DeleteMapping
    public void delete(@Valid @RequestBody MerchantDeleteDto request) {
        merchantService.deleteById(request.getIds());
    }

    @DeleteMapping("/{id}")
    public void delete(Long id) {
        merchantService.deleteById(id);
    }

}
