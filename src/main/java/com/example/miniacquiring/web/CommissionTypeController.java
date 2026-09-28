package com.example.miniacquiring.web;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetReferenceEntityResponse;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commission-type")
@RequiredArgsConstructor
public class CommissionTypeController {

    private final CommissionTypeRepository commissionTypeRepository;
    private final DtoMapper dtoMapper;

    @GetMapping
    public List<GetReferenceEntityResponse> getCommissionTypes() {
        var commissionTypes = commissionTypeRepository.findAll();
        return commissionTypes.stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

}
