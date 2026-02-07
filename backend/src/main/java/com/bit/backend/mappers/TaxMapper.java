package com.bit.backend.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import com.bit.backend.dtos.TaxDto;
import com.bit.backend.entities.TaxEntity;

@Repository
@Mapper(componentModel = "spring")
public interface TaxMapper {

    TaxDto toTaxDto(TaxEntity taxEntity);

    TaxEntity toTaxEntity(TaxDto taxDto);

    List<TaxDto> toTaxDtoList(List<TaxEntity> taxEntityList);

}
