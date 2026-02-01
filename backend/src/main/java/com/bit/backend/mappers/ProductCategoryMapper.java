package com.bit.backend.mappers;

import com.bit.backend.dtos.ProductCategoryDto;
import com.bit.backend.entities.ProductCategoryEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    ProductCategoryDto toProductCategoryDto(ProductCategoryEntity productCategoryEntity);
    ProductCategoryEntity toProductCategoryEntity(ProductCategoryDto productCategoryDto);
    List <ProductCategoryDto> toProductCategoryDtoList(List<ProductCategoryEntity> productCategoryEntityList);
}
