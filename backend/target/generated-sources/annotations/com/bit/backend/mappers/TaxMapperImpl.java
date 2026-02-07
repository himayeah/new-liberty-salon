package com.bit.backend.mappers;

import com.bit.backend.dtos.TaxDto;
import com.bit.backend.entities.TaxEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class TaxMapperImpl implements TaxMapper {

    @Override
    public TaxDto toTaxDto(TaxEntity taxEntity) {
        if ( taxEntity == null ) {
            return null;
        }

        TaxDto taxDto = new TaxDto();

        taxDto.setEffectiveDate( taxEntity.getEffectiveDate() );
        taxDto.setIsActive( taxEntity.getIsActive() );
        taxDto.setTaxName( taxEntity.getTaxName() );
        taxDto.setTaxRate( taxEntity.getTaxRate() );

        return taxDto;
    }

    @Override
    public TaxEntity toTaxEntity(TaxDto taxDto) {
        if ( taxDto == null ) {
            return null;
        }

        TaxEntity taxEntity = new TaxEntity();

        taxEntity.setEffectiveDate( taxDto.getEffectiveDate() );
        taxEntity.setIsActive( taxDto.getIsActive() );
        taxEntity.setTaxName( taxDto.getTaxName() );
        taxEntity.setTaxRate( taxDto.getTaxRate() );

        return taxEntity;
    }

    @Override
    public List<TaxDto> toTaxDtoList(List<TaxEntity> taxEntityList) {
        if ( taxEntityList == null ) {
            return null;
        }

        List<TaxDto> list = new ArrayList<TaxDto>( taxEntityList.size() );
        for ( TaxEntity taxEntity : taxEntityList ) {
            list.add( toTaxDto( taxEntity ) );
        }

        return list;
    }
}
