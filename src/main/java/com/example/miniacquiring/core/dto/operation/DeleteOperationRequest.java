package com.example.miniacquiring.core.dto.operation;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record DeleteOperationRequest(

        @NotEmpty
        List<Long> ids) {

}
