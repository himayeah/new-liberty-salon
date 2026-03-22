package com.bit.backend.mappers;

import com.bit.backend.dtos.ClientNotesDto;
import com.bit.backend.entities.ClientNotesEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ClientNotesMapper {
    ClientNotesDto toClientNotesDto(ClientNotesEntity clientNotesEntity);

    ClientNotesEntity toClientNotesEntity(ClientNotesDto clientNotesDto);

    List<ClientNotesDto> toClientNotesDtoList(List<ClientNotesEntity> clientNotesEntityList);

}
