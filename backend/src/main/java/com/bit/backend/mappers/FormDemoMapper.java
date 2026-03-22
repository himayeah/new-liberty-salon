package com.bit.backend.mappers;

import com.bit.backend.dtos.FormDemoDto;
import com.bit.backend.entities.FormDemoEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface FormDemoMapper {

    FormDemoDto toFormDemoDto(FormDemoEntity formDemoEntity);
    FormDemoEntity toFormDemoEntity(FormDemoDto formDemoDto);
}
