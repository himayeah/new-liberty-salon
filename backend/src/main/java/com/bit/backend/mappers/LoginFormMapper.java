package com.bit.backend.mappers;

import ch.qos.logback.core.model.ComponentModel;
import com.bit.backend.dtos.LoginFormDto;
import com.bit.backend.entities.LoginFormEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel ="spring", builder = @Builder(disableBuilder = true))
public interface LoginFormMapper {

    LoginFormDto toLoginFormDto(LoginFormEntity loginFormEntity);
    LoginFormEntity toLoginFormEntity(LoginFormDto loginFormDto);
}
