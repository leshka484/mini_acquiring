package com.example.miniacquiring.web;

import com.example.miniacquiring.core.dto.operation.CreateOperationRequest;
import com.example.miniacquiring.core.dto.operation.DeleteOperationRequest;
import com.example.miniacquiring.core.dto.operation.GetOperationResponse;
import com.example.miniacquiring.core.dto.operation.UpdateOperationRequest;
import com.example.miniacquiring.service.OperationService;
import com.example.miniacquiring.web.doc.OperationControllerDoc;
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
@RequestMapping("/operations")
@RequiredArgsConstructor
public class OperationController implements OperationControllerDoc {

    private final OperationService operationService;

    @PostMapping
    public GetOperationResponse create(@Valid @RequestBody CreateOperationRequest request) {
        return operationService.create(request);
    }

    @GetMapping("/{id}")
    public GetOperationResponse getById(Long id) {
        return operationService.getById(id);
    }

    @GetMapping
    public Page<GetOperationResponse> getAll(
            @ParameterObject
            @PageableDefault(size = 20) Pageable pageable) {
        return operationService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public GetOperationResponse update(Long id, @Valid @RequestBody UpdateOperationRequest request) {
        return operationService.update(id, request);
    }

    @DeleteMapping
    public void delete(@Valid @RequestBody DeleteOperationRequest request) {
        operationService.deleteById(request.ids());
    }

    @DeleteMapping("/{id}")
    public void delete(Long id) {
        operationService.deleteById(id);
    }

}
