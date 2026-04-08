package com.bit.backend.mappers;

import com.bit.backend.dtos.BillingPurchaseDto;
import com.bit.backend.entities.BillingPurchaseEntity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BillingPurchaseMapper {
    BillingPurchaseDto toDto(BillingPurchaseEntity entity);
    BillingPurchaseEntity toEntity(BillingPurchaseDto dto);
    List<BillingPurchaseDto> toDtoList(List<BillingPurchaseEntity> entities);
    List<BillingPurchaseEntity> toEntityList(List<BillingPurchaseDto> dtos);
}
