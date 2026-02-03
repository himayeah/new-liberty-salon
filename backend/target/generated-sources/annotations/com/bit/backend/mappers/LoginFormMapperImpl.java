package com.bit.backend.mappers;

import com.bit.backend.dtos.LoginFormDto;
import com.bit.backend.entities.LoginFormEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T22:45:01+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
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
