package com.example.miniacquiring.web;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.dto.DeleteOperationRequest;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.PayRequest;
import com.example.miniacquiring.core.dto.UpsertOperationRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    public void create(@Valid @RequestBody UpsertOperationRequest request) {
        operationService.create(request);
    }

    @GetMapping("/{id}")
    public GetOperationResponse getById(@PathVariable Long id) {
        return operationService.getById(id);
    }

    @GetMapping
    public Page<GetOperationResponse> getAll(
            @ParameterObject
            @PageableDefault(size = Const.PER_PAGE) Pageable pageable) {
        return operationService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody UpsertOperationRequest request) {
        operationService.update(id, request);
    }

    @PutMapping("/payment")
    public void payment(@Valid @RequestBody PayRequest request) {
        operationService.processPayment(request);
    }

    @PutMapping("/{id}/cancellation")
    public void cancellation(@PathVariable Long id) {
        operationService.cancelOperation(id);
    }
}
