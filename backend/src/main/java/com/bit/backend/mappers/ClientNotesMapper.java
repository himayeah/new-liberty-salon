package com.bit.backend.mappers;

import com.bit.backend.dtos.ClientNotesDto;
import com.bit.backend.entities.ClientNotesEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper(componentModel = "spring")
public interface ClientNotesMapper {
    ClientNotesDto toClientNotesDto(ClientNotesEntity clientNotesEntity);
    ClientNotesEntity toClientNotesEntity(ClientNotesDto clientNotesDto);
    List<ClientNotesDto> toClientNotesDtoList(List<ClientNotesEntity> clientNotesEntityList);

    Optional<ClientNotesEntity> findById(long id);
}
