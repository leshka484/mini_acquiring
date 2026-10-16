package com.example.miniacquiring.web;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetReferenceEntityResponse;
import com.example.miniacquiring.storage.repository.CommissionTypeRepository;
import com.example.miniacquiring.storage.repository.MerchantStatusRepository;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReferenceEntityController {
    
    private final CommissionTypeRepository commissionTypeRepository;
    private final MerchantStatusRepository merchantStatusRepository;
    private final OperationStatusRepository operationStatusRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final DtoMapper dtoMapper;

    @GetMapping("/commission-type")
    public List<GetReferenceEntityResponse> getCommissionTypes() {
        var commissionTypes = commissionTypeRepository.findAll();
        return commissionTypes.stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

    @GetMapping("/merchant-status")
    public List<GetReferenceEntityResponse> getMerchantStatuses() {
        var merchantStatuses = merchantStatusRepository.findAll();
        return merchantStatuses.stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

    @GetMapping("/operation-status")
    public List<GetReferenceEntityResponse> getOperationStatuses() {
        var operationStatuses = operationStatusRepository.findAll();
        return operationStatuses.stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

    @GetMapping("/operation-type")
    public List<GetReferenceEntityResponse> getOperationTypes() {
        var operationTypes = operationTypeRepository.findAll();
        return operationTypes.stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

}
