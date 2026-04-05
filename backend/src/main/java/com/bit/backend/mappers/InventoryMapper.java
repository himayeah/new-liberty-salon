package com.bit.backend.mappers;

import com.bit.backend.dtos.InventoryDto;
import com.bit.backend.entities.InventoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "productId", source = "product.id")
    InventoryDto toDto(InventoryEntity entity);

    @Mapping(target = "product.id", source = "productId")
    InventoryEntity toEntity(InventoryDto dto);

    List<InventoryDto> toDtoList(List<InventoryEntity> entities);
}
