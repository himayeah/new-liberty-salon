package com.bit.backend.mappers;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.entities.ClientRegEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-01T12:27:52+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ClientRegMapperImpl implements ClientRegMapper {

    @Override
    public ClientRegDto toClientRegDto(ClientRegEntity clientRegEntity) {
        if ( clientRegEntity == null ) {
            return null;
        }

        ClientRegDto clientRegDto = new ClientRegDto();

        clientRegDto.setAllergies( clientRegEntity.getAllergies() );
        clientRegDto.setDateOfBirth( clientRegEntity.getDateOfBirth() );
        clientRegDto.setEmail( clientRegEntity.getEmail() );
        clientRegDto.setFirstName( clientRegEntity.getFirstName() );
        clientRegDto.setGender( clientRegEntity.getGender() );
        clientRegDto.setLastName( clientRegEntity.getLastName() );
        clientRegDto.setLastVisitedDate( clientRegEntity.getLastVisitedDate() );
        clientRegDto.setLifetimeValue( clientRegEntity.getLifetimeValue() );
        clientRegDto.setPhoneNumber( clientRegEntity.getPhoneNumber() );
        clientRegDto.setPreferredStylist( clientRegEntity.getPreferredStylist() );
        clientRegDto.setTotalVisits( clientRegEntity.getTotalVisits() );

        return clientRegDto;
    }

    @Override
    public ClientRegEntity toClientRegEntity(ClientRegDto clientRegDto) {
        if ( clientRegDto == null ) {
            return null;
        }

        ClientRegEntity clientRegEntity = new ClientRegEntity();

        clientRegEntity.setAllergies( clientRegDto.getAllergies() );
        clientRegEntity.setDateOfBirth( clientRegDto.getDateOfBirth() );
        clientRegEntity.setEmail( clientRegDto.getEmail() );
        clientRegEntity.setFirstName( clientRegDto.getFirstName() );
        clientRegEntity.setGender( clientRegDto.getGender() );
        clientRegEntity.setLastName( clientRegDto.getLastName() );
        clientRegEntity.setLastVisitedDate( clientRegDto.getLastVisitedDate() );
        clientRegEntity.setLifetimeValue( clientRegDto.getLifetimeValue() );
        clientRegEntity.setPhoneNumber( clientRegDto.getPhoneNumber() );
        clientRegEntity.setPreferredStylist( clientRegDto.getPreferredStylist() );
        clientRegEntity.setTotalVisits( clientRegDto.getTotalVisits() );

        return clientRegEntity;
    }

    @Override
    public List<ClientRegDto> toClientRegDtoList(List<ClientRegEntity> clientRegEntityList) {
        if ( clientRegEntityList == null ) {
            return null;
        }

        List<ClientRegDto> list = new ArrayList<ClientRegDto>( clientRegEntityList.size() );
        for ( ClientRegEntity clientRegEntity : clientRegEntityList ) {
            list.add( toClientRegDto( clientRegEntity ) );
        }

        return list;
    }
}
