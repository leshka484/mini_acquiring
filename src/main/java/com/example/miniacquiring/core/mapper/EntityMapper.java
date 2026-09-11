package com.example.miniacquiring.core.mapper;

import com.example.miniacquiring.core.dto.merchant.CreateMerchantRequest;
import com.example.miniacquiring.core.dto.merchant.GetMerchantResponse;
import com.example.miniacquiring.core.dto.operation.CreateOperationRequest;
import com.example.miniacquiring.core.dto.operation.GetOperationResponse;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper { //TODO: DtoMapper

    @Mapping(target = "commissionType", ignore = true)
    MerchantEntity toEntity(CreateMerchantRequest request); //TODO: dead code

    @Mapping(target = "commissionType", source = "commissionType.type")
    GetMerchantResponse toResponse(MerchantEntity merchant);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "status", ignore = true)
    OperationEntity toEntity(CreateOperationRequest request); //TODO: dead code

    @Mapping(target = "merchant", source = "merchant.name")
    @Mapping(target = "type", source = "type.type")
    @Mapping(target = "status", source = "status.status")
    GetOperationResponse toResponse(OperationEntity entity);

}
