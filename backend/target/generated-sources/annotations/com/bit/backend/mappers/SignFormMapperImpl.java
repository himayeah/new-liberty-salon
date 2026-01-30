package com.bit.backend.mappers;

import com.bit.backend.dtos.SignFormDto;
import com.bit.backend.entities.SignFormEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-27T23:06:19+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class SignFormMapperImpl implements SignFormMapper {

    @Override
    public SignFormDto toSignFormDto(SignFormEntity signFormEntity) {
        if ( signFormEntity == null ) {
            return null;
        }

        SignFormDto signFormDto = new SignFormDto();

        signFormDto.setId( signFormEntity.getId() );
        signFormDto.setFirstName( signFormEntity.getFirstName() );
        signFormDto.setLastName( signFormEntity.getLastName() );
        signFormDto.setEmail( signFormEntity.getEmail() );
        signFormDto.setAge( signFormEntity.getAge() );

        return signFormDto;
    }

    @Override
    public SignFormEntity toSignFormEntity(SignFormDto signFormDto) {
        if ( signFormDto == null ) {
            return null;
        }

        SignFormEntity signFormEntity = new SignFormEntity();

        signFormEntity.setId( signFormDto.getId() );
        signFormEntity.setFirstName( signFormDto.getFirstName() );
        signFormEntity.setLastName( signFormDto.getLastName() );
        signFormEntity.setEmail( signFormDto.getEmail() );
        signFormEntity.setAge( signFormDto.getAge() );

        return signFormEntity;
    }

    @Override
    public List<SignFormDto> signFormDtoList(List<SignFormEntity> signFormEntities) {
        if ( signFormEntities == null ) {
            return null;
        }

        List<SignFormDto> list = new ArrayList<SignFormDto>( signFormEntities.size() );
        for ( SignFormEntity signFormEntity : signFormEntities ) {
            list.add( toSignFormDto( signFormEntity ) );
        }

        return list;
    }

    @Override
    public List<SignFormDto> toSignFormDtoList(List<SignFormEntity> signFormEntityList) {
        if ( signFormEntityList == null ) {
            return null;
        }

        List<SignFormDto> list = new ArrayList<SignFormDto>( signFormEntityList.size() );
        for ( SignFormEntity signFormEntity : signFormEntityList ) {
            list.add( toSignFormDto( signFormEntity ) );
        }

        return list;
    }
}
