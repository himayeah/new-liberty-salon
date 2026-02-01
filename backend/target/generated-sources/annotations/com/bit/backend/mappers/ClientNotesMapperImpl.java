package com.bit.backend.mappers;

import com.bit.backend.dtos.ClientNotesDto;
import com.bit.backend.entities.ClientNotesEntity;
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
public class ClientNotesMapperImpl implements ClientNotesMapper {

    @Override
    public ClientNotesDto toClientNotesDto(ClientNotesEntity clientNotesEntity) {
        if ( clientNotesEntity == null ) {
            return null;
        }

        ClientNotesDto clientNotesDto = new ClientNotesDto();

        clientNotesDto.setClientName( clientNotesEntity.getClientName() );
        clientNotesDto.setNoteContent( clientNotesEntity.getNoteContent() );
        clientNotesDto.setNoteDate( clientNotesEntity.getNoteDate() );
        clientNotesDto.setNoteType( clientNotesEntity.getNoteType() );
        clientNotesDto.setStylistName( clientNotesEntity.getStylistName() );

        return clientNotesDto;
    }

    @Override
    public ClientNotesEntity toClientNotesEntity(ClientNotesDto clientNotesDto) {
        if ( clientNotesDto == null ) {
            return null;
        }

        ClientNotesEntity clientNotesEntity = new ClientNotesEntity();

        clientNotesEntity.setClientName( clientNotesDto.getClientName() );
        clientNotesEntity.setNoteContent( clientNotesDto.getNoteContent() );
        clientNotesEntity.setNoteDate( clientNotesDto.getNoteDate() );
        clientNotesEntity.setNoteType( clientNotesDto.getNoteType() );
        clientNotesEntity.setStylistName( clientNotesDto.getStylistName() );

        return clientNotesEntity;
    }

    @Override
    public List<ClientNotesDto> toClientNotesDtoList(List<ClientNotesEntity> clientNotesEntityList) {
        if ( clientNotesEntityList == null ) {
            return null;
        }

        List<ClientNotesDto> list = new ArrayList<ClientNotesDto>( clientNotesEntityList.size() );
        for ( ClientNotesEntity clientNotesEntity : clientNotesEntityList ) {
            list.add( toClientNotesDto( clientNotesEntity ) );
        }

        return list;
    }
}
