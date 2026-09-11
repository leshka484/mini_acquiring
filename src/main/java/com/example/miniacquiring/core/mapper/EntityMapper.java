package com.example.miniacquiring.core.mapper;

import com.example.miniacquiring.core.dto.merchant.CreateMerchantRequest;
import com.example.miniacquiring.core.dto.merchant.GetMerchantResponse;
import com.example.miniacquiring.storage.entity.CommissionEntity;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import com.example.miniacquiring.storage.entity.OperationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    @Mapping(target = "commissionType", ignore = true)
    MerchantEntity toEntity(CreateMerchantRequest request);

    @Mapping(
            target = "commissionType",
            source = "commissionType.type")
    GetMerchantResponse toResponse(MerchantEntity merchant);

    //OperationEntity toEntity();
    //GetOperationEntity toResponse(OperationEntity entity);

    //CommissionEntity toEntity();
    //GetCommissionEntity toResponse(CommissionEntity entity);

}
