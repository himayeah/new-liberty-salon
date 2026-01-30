package com.bit.backend.mappers;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.entities.ClientRegEntity;
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
public class ClientRegMapperImpl implements ClientRegMapper {

    @Override
    public ClientRegDto toClientRegDto(ClientRegEntity clientRegEntity) {
        if ( clientRegEntity == null ) {
            return null;
        }

        ClientRegDto clientRegDto = new ClientRegDto();

        return clientRegDto;
    }

    @Override
    public ClientRegEntity toClientRegEntity(ClientRegDto clientRegDto) {
        if ( clientRegDto == null ) {
            return null;
        }

        ClientRegEntity clientRegEntity = new ClientRegEntity();

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
