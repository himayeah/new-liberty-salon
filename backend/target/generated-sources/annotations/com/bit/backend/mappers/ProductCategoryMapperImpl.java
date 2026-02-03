package com.bit.backend.mappers;

import com.bit.backend.dtos.ProductCategoryDto;
import com.bit.backend.entities.ProductCategoryEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T22:45:01+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public ProductCategoryDto toProductCategoryDto(ProductCategoryEntity productCategoryEntity) {
        if ( productCategoryEntity == null ) {
            return null;
        }

        ProductCategoryDto productCategoryDto = new ProductCategoryDto();

        productCategoryDto.setProductCategoryName( productCategoryEntity.getProductCategoryName() );

        return productCategoryDto;
    }

    @Override
    public ProductCategoryEntity toProductCategoryEntity(ProductCategoryDto productCategoryDto) {
        if ( productCategoryDto == null ) {
            return null;
        }

        ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();

        productCategoryEntity.setProductCategoryName( productCategoryDto.getProductCategoryName() );

        return productCategoryEntity;
    }

    @Override
    public List<ProductCategoryDto> toProductCategoryDtoList(List<ProductCategoryEntity> productCategoryEntityList) {
        if ( productCategoryEntityList == null ) {
            return null;
        }

        List<ProductCategoryDto> list = new ArrayList<ProductCategoryDto>( productCategoryEntityList.size() );
        for ( ProductCategoryEntity productCategoryEntity : productCategoryEntityList ) {
            list.add( toProductCategoryDto( productCategoryEntity ) );
        }

        return list;
    }
}
