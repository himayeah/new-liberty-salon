package com.bit.backend.mappers;

import com.bit.backend.dtos.ClientNotesDto;
import com.bit.backend.entities.ClientNotesEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T21:43:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
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
        clientNotesDto.setStylistName( clientNotesEntity.getStylistName() );
        clientNotesDto.setNoteType( clientNotesEntity.getNoteType() );
        clientNotesDto.setNoteContent( clientNotesEntity.getNoteContent() );
        clientNotesDto.setNoteDate( clientNotesEntity.getNoteDate() );

        return clientNotesDto;
    }

    @Override
    public ClientNotesEntity toClientNotesEntity(ClientNotesDto clientNotesDto) {
        if ( clientNotesDto == null ) {
            return null;
        }

        ClientNotesEntity clientNotesEntity = new ClientNotesEntity();

        clientNotesEntity.setClientName( clientNotesDto.getClientName() );
        clientNotesEntity.setStylistName( clientNotesDto.getStylistName() );
        clientNotesEntity.setNoteType( clientNotesDto.getNoteType() );
        clientNotesEntity.setNoteContent( clientNotesDto.getNoteContent() );
        clientNotesEntity.setNoteDate( clientNotesDto.getNoteDate() );

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
