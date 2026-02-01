package com.bit.backend.mappers;

import com.bit.backend.dtos.LoginFormDto;
import com.bit.backend.entities.LoginFormEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-01T12:27:52+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class LoginFormMapperImpl implements LoginFormMapper {

    @Override
    public LoginFormDto toLoginFormDto(LoginFormEntity loginFormEntity) {
        if ( loginFormEntity == null ) {
            return null;
        }

        LoginFormDto loginFormDto = new LoginFormDto();

        return loginFormDto;
    }

    @Override
    public LoginFormEntity toLoginFormEntity(LoginFormDto loginFormDto) {
        if ( loginFormDto == null ) {
            return null;
        }

        LoginFormEntity loginFormEntity = new LoginFormEntity();

        return loginFormEntity;
    }
}
