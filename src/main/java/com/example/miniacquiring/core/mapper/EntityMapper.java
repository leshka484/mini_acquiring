package com.example.miniacquiring.core.mapper;

import com.example.miniacquiring.core.dto.GetMerchantResponse;
import com.example.miniacquiring.core.dto.GetOperationResponse;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    @Mapping(target = "commissionType", source = "commissionType.type")
    @Mapping(target = "status", source = "status.status")
    GetMerchantResponse toResponse(MerchantEntity merchant);

    @Mapping(target = "merchant", source = "merchant.name")
    @Mapping(target = "type", source = "type.type")
    @Mapping(target = "status", source = "status.name")
    GetOperationResponse toResponse(OperationEntity entity);

}
