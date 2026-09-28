package com.example.miniacquiring.web;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetReferenceEntityResponse;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operation-type")
@RequiredArgsConstructor
public class OperationTypeController {

    private final OperationTypeRepository operationTypeRepository;
    private final DtoMapper dtoMapper;

    @GetMapping
    public List<GetReferenceEntityResponse> getCommissionTypes() {
        var operationTypes = operationTypeRepository.findAll();
        return operationTypes.stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

}
