package com.bit.backend.services.impl;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.entities.ClientRegEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.ClientRegMapper;
import com.bit.backend.repositories.ClientRegRepository;
import com.bit.backend.services.ClientRegServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientRegService implements ClientRegServiceI {

    private final ClientRegRepository clientRegRepository;
    private final ClientRegMapper clientRegMapper;

    public ClientRegService(ClientRegRepository clientRegRepository, ClientRegMapper clientRegMapper) {
        this.clientRegRepository = clientRegRepository;
        this.clientRegMapper = clientRegMapper;
    }

    @Override
    public ClientRegDto addClientReg(ClientRegDto clientRegDto) {
        System.out.println("Data Object 2 : " + clientRegDto);

        System.out.println("Client First Name 2  :" + clientRegDto.getFirstName());
        try {
            //Converting DTO to Entity and Entity to DTO
            // clientRegDto.setPassword("123456"); // Removed as setPassword doesn't exist in DTO
            ClientRegEntity clientRegEntity = clientRegMapper.toClientRegEntity(clientRegDto);
            System.out.println("Client First Name 3  :" + clientRegEntity.getFirstName());
            ClientRegEntity savedItem = clientRegRepository.save(clientRegEntity);
            ClientRegDto savedDto = clientRegMapper.toClientRegDto(savedItem);

            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<ClientRegDto> getData() {

        try {
            List<ClientRegEntity> clientRegEntityList = clientRegRepository.findAll();
            List<ClientRegDto> clientRegDtoList = clientRegMapper.toClientRegDtoList(clientRegEntityList);
            return clientRegDtoList;
        }
        catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClientRegDto updateClientReg(long id, ClientRegDto clientRegDto) {
        System.out.println("Hit ");
        try{
        Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findById(id);
        if (!optionalClientRegEntity.isPresent()) {
            throw new AppException("Client Reg Does Not Exist", HttpStatus.BAD_REQUEST);
        }

            ClientRegEntity newClientRegEntity = clientRegMapper.toClientRegEntity(clientRegDto);
            newClientRegEntity.setId(id);
            ClientRegEntity clientRegEntity = clientRegRepository.save(newClientRegEntity);
            ClientRegDto clientRegDtoRes = clientRegMapper.toClientRegDto(clientRegEntity);
            System.out.println("update Successfully: " + clientRegDtoRes.getFirstName());
            return clientRegDtoRes;
    }
        catch (Exception e) {
        throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);}
    }

    @Override
    public ClientRegDto deleteClientReg(long id) {

        try{
        Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findById(id);
        /*optional means a container that may or may not have a value. This is used to avoid null errors*/
        if (!optionalClientRegEntity.isPresent()) {
            throw new AppException("Client Reg Does Not Exist", HttpStatus.BAD_REQUEST);
        }

        clientRegRepository.deleteById(id);
        return clientRegMapper.toClientRegDto(optionalClientRegEntity.get());

    }
        catch (Exception e) {
        throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);}
    }


}
