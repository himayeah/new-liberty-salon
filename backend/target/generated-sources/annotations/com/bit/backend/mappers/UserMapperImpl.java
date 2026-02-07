package com.bit.backend.mappers;

import com.bit.backend.dtos.SignUpDto;
import com.bit.backend.dtos.UserDto;
import com.bit.backend.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName() );
        userDto.setLogin( user.getLogin() );

        return userDto;
    }

    @Override
    public User signUpToUser(SignUpDto signUpDto) {
        if ( signUpDto == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( signUpDto.firstName() );
        user.setLastName( signUpDto.lastName() );
        user.setLogin( signUpDto.login() );

        return user;
    }
}
