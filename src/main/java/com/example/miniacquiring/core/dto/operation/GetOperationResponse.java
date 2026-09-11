package com.example.miniacquiring.core.dto.operation;

import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetOperationResponse(
        String merchant,
        String status,
        BigDecimal sum,
        String type,
        Long parentId,
        LocalDateTime createdAt,
        LocalDateTime processedAt) {

}
