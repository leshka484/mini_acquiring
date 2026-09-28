package com.example.miniacquiring.core;

import com.example.miniacquiring.core.dto.GetCommissionResponse;
import com.example.miniacquiring.core.dto.GetMerchantResponse;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.core.dto.GetReferenceEntityResponse;
import com.example.miniacquiring.service.utils.MoneyUtils;
import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    @Mapping(target = "commissionType", source = "commissionType.name")
    @Mapping(target = "status", source = "status.name")
    GetMerchantResponse toResponse(MerchantEntity merchant);

    @Mapping(target = "merchant", source = "merchant.name")
    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "status", source = "status.name")
    GetOperationResponse toResponse(OperationEntity entity);

    @Mapping(target = "operation", source = "operation.id")
    GetCommissionResponse toResponse(CommissionEntity entity);

    GetReferenceEntityResponse toResponse(CommissionTypeEntity entity);

    GetReferenceEntityResponse toResponse(MerchantStatusEntity entity);

    GetReferenceEntityResponse toResponse(OperationStatusEntity entity);

    GetReferenceEntityResponse toResponse(OperationTypeEntity entity);

    default BigDecimal map(Long kopecks) {
        return MoneyUtils.convert(kopecks);
    }

}
