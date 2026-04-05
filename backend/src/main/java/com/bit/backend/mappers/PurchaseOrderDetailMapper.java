package com.bit.backend.mappers;

import com.bit.backend.dtos.PurchaseOrderDetailDto;
import com.bit.backend.entities.PurchaseOrderDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseOrderDetailMapper {

    @Mapping(target = "purchaseOrder.id", source = "purchaseOrderId")
    @Mapping(target = "product.id", source = "productId")
    PurchaseOrderDetailEntity toEntity(PurchaseOrderDetailDto dto);

    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.productName")
    PurchaseOrderDetailDto toDto(PurchaseOrderDetailEntity entity);

    List<PurchaseOrderDetailDto> toDtoList(List<PurchaseOrderDetailEntity> entities);
}
