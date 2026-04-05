package com.bit.backend.services.impl;

import com.bit.backend.dtos.ClientNotesDto;
import com.bit.backend.entities.ClientNotesEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.ClientNotesMapper;
import com.bit.backend.repositories.ClientNotesRepository;
import com.bit.backend.services.ClientNotesServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientNotesService implements ClientNotesServiceI {
    private final ClientNotesRepository clientNotesRepository;
    private final ClientNotesMapper clientNotesMapper;

    public ClientNotesService(ClientNotesRepository clientNotesRepository, ClientNotesMapper clientNotesMapper) {
        this.clientNotesRepository = clientNotesRepository;
        this.clientNotesMapper = clientNotesMapper;
    }

    @Override
    public ClientNotesDto addClientNote(ClientNotesDto clientNotesDto) {
        try {
            ClientNotesEntity clientNotesEntity = clientNotesMapper.toClientNotesEntity(clientNotesDto);
            ClientNotesEntity savedItem = clientNotesRepository.save(clientNotesEntity);
            ClientNotesDto savedDto = clientNotesMapper.toClientNotesDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<ClientNotesDto> getClientNote() {
        try {
            List<ClientNotesEntity> clientNotesEntityList = clientNotesRepository.findAll();
            List<ClientNotesDto> clientNotesDtoList = clientNotesMapper.toClientNotesDtoList(clientNotesEntityList);
            return clientNotesDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClientNotesDto updateClientNote(long id, ClientNotesDto clientNotesDto) {
        try {
            Optional<ClientNotesEntity> optionalClientNotesEntity = clientNotesRepository.findById(id);
            if (!optionalClientNotesEntity.isPresent()) {
                throw new AppException("Client Note does Not Exist", HttpStatus.BAD_REQUEST);
            }
            ClientNotesEntity newClientNotesEntity = clientNotesMapper.toClientNotesEntity(clientNotesDto);
            newClientNotesEntity.setId(id);
            ClientNotesEntity clientNotesEntity = clientNotesRepository.save(newClientNotesEntity);
            ClientNotesDto clientNotesDtoRes = clientNotesMapper.toClientNotesDto(clientNotesEntity);
            System.out.println("update Successfully: " + clientNotesDtoRes.getClientName());
            return clientNotesDtoRes;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ClientNotesDto deleteClientNote(long id) {
        try {
            Optional<ClientNotesEntity> optionalClientNotesEntity = clientNotesRepository.findById(id);
            if (!optionalClientNotesEntity.isPresent()) {
                throw new AppException("Client Note does Not Exist", HttpStatus.BAD_REQUEST);
            }
            clientNotesRepository.deleteById(id);
            return clientNotesMapper.toClientNotesDto(optionalClientNotesEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }
}