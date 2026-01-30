package com.bit.backend.services;

import com.bit.backend.dtos.ClientNotesDto;

import java.util.List;

public interface ClientNotesServiceI {
    ClientNotesDto addClientNote(ClientNotesDto clientNotesDto);
    List<ClientNotesDto> getClientNote();
    ClientNotesDto updateClientNote(long id, ClientNotesDto clientNotesDto);
    ClientNotesDto deleteClientNote(long id);
}
