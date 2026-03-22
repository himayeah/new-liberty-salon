package com.bit.backend.mappers;

import com.bit.backend.dtos.FormDemoDto;
import com.bit.backend.dtos.SignFormDto;
import com.bit.backend.entities.FormDemoEntity;
import com.bit.backend.entities.SignFormEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SignFormMapper {

    SignFormDto toSignFormDto(SignFormEntity signFormEntity);
    SignFormEntity toSignFormEntity(SignFormDto signFormDto);
    List<SignFormDto> signFormDtoList (List<SignFormEntity> signFormEntities);

    List<SignFormDto> toSignFormDtoList(List<SignFormEntity> signFormEntityList);
}