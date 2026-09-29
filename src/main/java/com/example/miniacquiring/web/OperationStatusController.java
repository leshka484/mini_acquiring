package com.example.miniacquiring.web;

import com.example.miniacquiring.core.DtoMapper;
import com.example.miniacquiring.core.dto.GetReferenceEntityResponse;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operation-status")
@RequiredArgsConstructor
public class OperationStatusController {

    private final OperationStatusRepository operationStatusRepository;
    private final DtoMapper dtoMapper;

    @GetMapping
    public List<GetReferenceEntityResponse> getCommissionTypes() {
        var operationStatuses = operationStatusRepository.findAll();
        return operationStatuses.stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

}
