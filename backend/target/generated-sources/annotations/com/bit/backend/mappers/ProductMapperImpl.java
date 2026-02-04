package com.bit.backend.mappers;

import com.bit.backend.dtos.ProductDto;
import com.bit.backend.entities.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
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

        productDto.setBarcode( productEntity.getBarcode() );
        productDto.setBrand( productEntity.getBrand() );
        productDto.setCategoryName( productEntity.getCategoryName() );
        productDto.setId( productEntity.getId() );
        productDto.setProductDescription( productEntity.getProductDescription() );
        productDto.setProductName( productEntity.getProductName() );
        productDto.setPurchasePrice( productEntity.getPurchasePrice() );
        productDto.setReOrderLevel( productEntity.getReOrderLevel() );
        productDto.setSellingPrice( productEntity.getSellingPrice() );
        productDto.setSku( productEntity.getSku() );
        productDto.setTaxable( productEntity.isTaxable() );
        productDto.setUnit( productEntity.getUnit() );

        return productDto;
    }

    @Override
    public ProductEntity toProductEntity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setBarcode( productDto.getBarcode() );
        productEntity.setBrand( productDto.getBrand() );
        productEntity.setCategoryName( productDto.getCategoryName() );
        productEntity.setId( productDto.getId() );
        productEntity.setProductDescription( productDto.getProductDescription() );
        productEntity.setProductName( productDto.getProductName() );
        productEntity.setPurchasePrice( productDto.getPurchasePrice() );
        productEntity.setReOrderLevel( productDto.getReOrderLevel() );
        productEntity.setSellingPrice( productDto.getSellingPrice() );
        productEntity.setSku( productDto.getSku() );
        productEntity.setTaxable( productDto.isTaxable() );
        productEntity.setUnit( productDto.getUnit() );

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
