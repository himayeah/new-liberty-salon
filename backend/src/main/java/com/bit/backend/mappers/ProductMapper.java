package com.bit.backend.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.bit.backend.dtos.ProductDto;
import com.bit.backend.entities.ProductEntity;

@Repository
@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toProductDto(ProductEntity productEntity);

    ProductEntity toProductEntity(ProductDto productDto);

    List<ProductDto> toProductDtoList(List<ProductEntity> productEntityList);

}
