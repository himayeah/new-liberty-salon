package com.bit.backend.controllers;

import com.bit.backend.dtos.ClientNotesDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ClientNotesServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/client-notes")
public class ClientNotesController {

    private final ClientNotesServiceI clientNotesServiceI;

    public ClientNotesController(ClientNotesServiceI clientNotesServiceI) {
        this.clientNotesServiceI = clientNotesServiceI;
    }

    @PostMapping
    public ResponseEntity<ClientNotesDto> addClientNote(@RequestBody ClientNotesDto clientNotesDto)
            throws AppException {
        try {
            ClientNotesDto clientNotesDtoResponse = clientNotesServiceI.addClientNote(clientNotesDto);
            return ResponseEntity.created(URI.create("/client-notes/" + clientNotesDtoResponse.getClientName()))
                    .body(clientNotesDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClientNotesDto>> getClientNote() {
        try {
            List<ClientNotesDto> clientNotesDtoList = clientNotesServiceI.getClientNote();
            return ResponseEntity.ok(clientNotesDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ClientNotesDto> updateClientNote(@PathVariable long id,
            @RequestBody ClientNotesDto clientNotesDto) {
        try {
            ClientNotesDto responseClientNotesDto = clientNotesServiceI.updateClientNote(id, clientNotesDto);
            return ResponseEntity.ok(responseClientNotesDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ClientNotesDto> deleteClientNote(@PathVariable long id) {
        try {
            ClientNotesDto clientNotesDto = clientNotesServiceI.deleteClientNote(id);
            return ResponseEntity.ok(clientNotesDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
