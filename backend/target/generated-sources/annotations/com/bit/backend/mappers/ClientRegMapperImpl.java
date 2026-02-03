package com.bit.backend.mappers;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.entities.ClientRegEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T22:45:01+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class ClientRegMapperImpl implements ClientRegMapper {

    @Override
    public ClientRegDto toClientRegDto(ClientRegEntity clientRegEntity) {
        if ( clientRegEntity == null ) {
            return null;
        }

        ClientRegDto clientRegDto = new ClientRegDto();

        clientRegDto.setFirstName( clientRegEntity.getFirstName() );
        clientRegDto.setLastName( clientRegEntity.getLastName() );
        clientRegDto.setEmail( clientRegEntity.getEmail() );
        clientRegDto.setPhoneNumber( clientRegEntity.getPhoneNumber() );
        clientRegDto.setDateOfBirth( clientRegEntity.getDateOfBirth() );
        clientRegDto.setGender( clientRegEntity.getGender() );
        clientRegDto.setPreferredStylist( clientRegEntity.getPreferredStylist() );
        clientRegDto.setAllergies( clientRegEntity.getAllergies() );
        clientRegDto.setTotalVisits( clientRegEntity.getTotalVisits() );
        clientRegDto.setLastVisitedDate( clientRegEntity.getLastVisitedDate() );
        clientRegDto.setLifetimeValue( clientRegEntity.getLifetimeValue() );

        return clientRegDto;
    }

    @Override
    public ClientRegEntity toClientRegEntity(ClientRegDto clientRegDto) {
        if ( clientRegDto == null ) {
            return null;
        }

        ClientRegEntity clientRegEntity = new ClientRegEntity();

        clientRegEntity.setFirstName( clientRegDto.getFirstName() );
        clientRegEntity.setLastName( clientRegDto.getLastName() );
        clientRegEntity.setEmail( clientRegDto.getEmail() );
        clientRegEntity.setPhoneNumber( clientRegDto.getPhoneNumber() );
        clientRegEntity.setDateOfBirth( clientRegDto.getDateOfBirth() );
        clientRegEntity.setGender( clientRegDto.getGender() );
        clientRegEntity.setPreferredStylist( clientRegDto.getPreferredStylist() );
        clientRegEntity.setAllergies( clientRegDto.getAllergies() );
        clientRegEntity.setTotalVisits( clientRegDto.getTotalVisits() );
        clientRegEntity.setLastVisitedDate( clientRegDto.getLastVisitedDate() );
        clientRegEntity.setLifetimeValue( clientRegDto.getLifetimeValue() );

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
