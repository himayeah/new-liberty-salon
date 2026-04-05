package com.bit.backend.mappers;

import com.bit.backend.dtos.GrnDto;
import com.bit.backend.entities.GrnEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GrnMapper {

    @Mapping(target = "purchaseOrder.id", source = "purchaseOrderId")
    @Mapping(target = "product.id", source = "productId")
    GrnEntity toEntity(GrnDto dto);

    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.productName")
    GrnDto toDto(GrnEntity entity);

    List<GrnDto> toDtoList(List<GrnEntity> entities);
}
