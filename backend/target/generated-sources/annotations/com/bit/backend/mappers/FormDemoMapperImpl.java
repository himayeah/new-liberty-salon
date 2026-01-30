package com.bit.backend.mappers;

import com.bit.backend.dtos.FormDemoDto;
import com.bit.backend.entities.FormDemoEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T22:58:52+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class FormDemoMapperImpl implements FormDemoMapper {

    @Override
    public FormDemoDto toFormDemoDto(FormDemoEntity formDemoEntity) {
        if ( formDemoEntity == null ) {
            return null;
        }

        FormDemoDto formDemoDto = new FormDemoDto();

        formDemoDto.setId( formDemoEntity.getId() );
        formDemoDto.setFirstName( formDemoEntity.getFirstName() );
        formDemoDto.setLastName( formDemoEntity.getLastName() );
        formDemoDto.setEmail( formDemoEntity.getEmail() );
        if ( formDemoEntity.getAge() != null ) {
            formDemoDto.setAge( Integer.parseInt( formDemoEntity.getAge() ) );
        }

        return formDemoDto;
    }

    @Override
    public FormDemoEntity toFormDemoEntity(FormDemoDto formDemoDto) {
        if ( formDemoDto == null ) {
            return null;
        }

        FormDemoEntity formDemoEntity = new FormDemoEntity();

        formDemoEntity.setId( formDemoDto.getId() );
        formDemoEntity.setFirstName( formDemoDto.getFirstName() );
        formDemoEntity.setLastName( formDemoDto.getLastName() );
        formDemoEntity.setAge( String.valueOf( formDemoDto.getAge() ) );
        formDemoEntity.setEmail( formDemoDto.getEmail() );

        return formDemoEntity;
    }
}
