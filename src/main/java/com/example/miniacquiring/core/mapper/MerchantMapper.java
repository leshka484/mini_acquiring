package com.example.miniacquiring.core.mapper;

import com.example.miniacquiring.core.dto.merchant.MerchantCreateDto;
import com.example.miniacquiring.core.dto.merchant.MerchantGetDto;
import com.example.miniacquiring.storage.entity.MerchantEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    @Mapping(target = "commissionType", ignore = true)
    MerchantEntity toEntity(MerchantCreateDto request);

    @Mapping(
            target = "commissionType",
            source = "commissionType.type")
    MerchantGetDto toResponse(MerchantEntity merchant);

    List<MerchantGetDto> toResponse(List<MerchantEntity> merchants);

}
