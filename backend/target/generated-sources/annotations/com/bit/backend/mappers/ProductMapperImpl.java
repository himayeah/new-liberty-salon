package com.bit.backend.mappers;

import com.bit.backend.dtos.ProductDto;
import com.bit.backend.entities.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T22:45:26+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toProductDto(ProductEntity productEntity) {
        if ( productEntity == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( productEntity.getId() );
        productDto.setProductName( productEntity.getProductName() );
        productDto.setCategoryName( productEntity.getCategoryName() );
        productDto.setBrand( productEntity.getBrand() );
        productDto.setProductDescription( productEntity.getProductDescription() );
        productDto.setUnit( productEntity.getUnit() );
        productDto.setPurchasePrice( productEntity.getPurchasePrice() );
        productDto.setSellingPrice( productEntity.getSellingPrice() );
        productDto.setBarcode( productEntity.getBarcode() );
        productDto.setSku( productEntity.getSku() );
        productDto.setTaxable( productEntity.isTaxable() );
        productDto.setReOrderLevel( productEntity.getReOrderLevel() );

        return productDto;
    }

    @Override
    public ProductEntity toProductEntity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setId( productDto.getId() );
        productEntity.setProductName( productDto.getProductName() );
        productEntity.setCategoryName( productDto.getCategoryName() );
        productEntity.setBrand( productDto.getBrand() );
        productEntity.setProductDescription( productDto.getProductDescription() );
        productEntity.setUnit( productDto.getUnit() );
        productEntity.setPurchasePrice( productDto.getPurchasePrice() );
        productEntity.setSellingPrice( productDto.getSellingPrice() );
        productEntity.setBarcode( productDto.getBarcode() );
        productEntity.setSku( productDto.getSku() );
        productEntity.setTaxable( productDto.isTaxable() );
        productEntity.setReOrderLevel( productDto.getReOrderLevel() );

        return productEntity;
    }

    @Override
    public List<ProductDto> toProductDtoList(List<ProductEntity> productEntityList) {
        if ( productEntityList == null ) {
            return null;
        }

        List<ProductDto> list = new ArrayList<ProductDto>( productEntityList.size() );
        for ( ProductEntity productEntity : productEntityList ) {
            list.add( toProductDto( productEntity ) );
        }

        return list;
    }
}
