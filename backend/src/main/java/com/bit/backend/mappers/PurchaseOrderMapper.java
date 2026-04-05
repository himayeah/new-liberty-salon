package com.bit.backend.mappers;

import com.bit.backend.dtos.PurchaseOrderDto;
import com.bit.backend.entities.PurchaseOrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    @Mapping(target = "supplier", ignore = false)
    @Mapping(target = "supplierId", source = "supplier.id")
    PurchaseOrderDto toDto(PurchaseOrderEntity entity);

    @Mapping(target = "supplier", ignore = true)
    PurchaseOrderEntity toEntity(PurchaseOrderDto dto);
}
