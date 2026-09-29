package com.example.miniacquiring.web;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetReferenceEntityResponse;
import com.example.miniacquiring.storage.repository.MerchantStatusRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant-status")
@RequiredArgsConstructor
public class MerchantStatusController {

    private final MerchantStatusRepository merchantStatusRepository;
    private final DtoMapper dtoMapper;

    @GetMapping
    public List<GetReferenceEntityResponse> getCommissionTypes() {
        var merchantStatuses = merchantStatusRepository.findAll();
        return merchantStatuses.stream()
                .map(dtoMapper::toResponse)
                .toList();
    }
}
