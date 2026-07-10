package com.bit.backend.services.impl;

import com.bit.backend.dtos.ClientLifeTimeValueDto;
import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.dtos.ClientRegTotalVisitsDto;
import com.bit.backend.entities.ClientRegEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.ClientRegMapper;
import com.bit.backend.repositories.ClientRegRepository;
import com.bit.backend.services.ClientRegServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

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
        try {
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
            System.out.println("Getting all Client Data: " + clientRegDtoList);
            return clientRegDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @Override
    // public ClientRegDto updateClientReg(long id, ClientRegDto clientRegDto) {
    // try {
    // Optional<ClientRegEntity> optionalClientRegEntity =
    // clientRegRepository.findById(id);
    // if (!optionalClientRegEntity.isPresent()) {
    // throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
    // }
    // ClientRegEntity newClientRegEntity =
    // clientRegMapper.toClientRegEntity(clientRegDto);
    // newClientRegEntity.setId(id);
    // ClientRegEntity clientRegEntity =
    // clientRegRepository.save(newClientRegEntity);
    // ClientRegDto clientRegDtoRes =
    // clientRegMapper.toClientRegDto(clientRegEntity);
    // System.out.println("update Successfully: " + clientRegDtoRes.getFirstName());
    // return clientRegDtoRes;
    // } catch (Exception e) {
    // throw new AppException("Request failed with error:" + e,
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    @Override
    public ClientRegDto updateClientReg(long id, ClientRegDto clientRegDto) {
        try {
            Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findById(id);
            if (!optionalClientRegEntity.isPresent()) {
                throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
            }
            ClientRegEntity newClientRegEntity = clientRegMapper.toClientRegEntity(clientRegDto);
            newClientRegEntity.setId(id);

            List<ClientLifeTimeValueDto> clientLifeTimeValueDtoList = clientRegRepository.getClientLifetimeValue();
            for (ClientLifeTimeValueDto clientLifeTimeValueDto : clientLifeTimeValueDtoList) {
                if (clientLifeTimeValueDto.getClientId() == id) {
                    newClientRegEntity.setLifetimeValue(clientLifeTimeValueDto.getLifetimeValue());
                    break;
                }
            }

            ClientRegEntity clientRegEntity = clientRegRepository.save(newClientRegEntity);
            ClientRegDto clientRegDtoRes = clientRegMapper.toClientRegDto(clientRegEntity);
            System.out.println("update Successfully: " + clientRegDtoRes.getFirstName());
            return clientRegDtoRes;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClientRegDto getById(long id) {
        try {
            Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findById(id);
            if (!optionalClientRegEntity.isPresent()) {
                throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
            }
            return clientRegMapper.toClientRegDto(optionalClientRegEntity.get());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClientRegDto deleteClientReg(long id) {

        try {
            Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findById(id);
            if (!optionalClientRegEntity.isPresent()) {
                throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
            }

            clientRegRepository.deleteById(id);
            return clientRegMapper.toClientRegDto(optionalClientRegEntity.get());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(
                    "Cannot delete this client because they have active appointments, billings, or other associated records.",
                    HttpStatus.CONFLICT);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Dashboard card (New Clients within last 30 days)
    @Override
    public long countClientRegistrationsLast30Days() {
        return clientRegRepository.countClientRegistrationsLast30Days();
    }

    // Search Client for Public Booking Login
    @Override
    public ClientRegDto findByFirstNameAndEmail(String firstName, String email) {
        try {
            Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findByFirstNameAndEmail(firstName,
                    email);
            if (!optionalClientRegEntity.isPresent()) {
                throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
            }
            return clientRegMapper.toClientRegDto(optionalClientRegEntity.get());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get Client Total Visits
    // run query, get id and count results, add them to clientreg entity columns so
    // they get saved in db
    // convert entity back to dto and send response back to controller
    @Override
    public List<ClientRegTotalVisitsDto> calculateClientVisits() {
        try {
            List<ClientRegTotalVisitsDto> clientRegTotalVisitsDtoList = clientRegRepository.getClientTotalVisits();
            System.out.println("Client Total Visits DTO List: " + clientRegTotalVisitsDtoList);
            for (ClientRegTotalVisitsDto clientRegTotalVisitsDto : clientRegTotalVisitsDtoList) {
                if (clientRegTotalVisitsDto.getClientId() != null) {
                    Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository
                            .findById(clientRegTotalVisitsDto.getClientId());
                    if (optionalClientRegEntity.isPresent()) {
                        ClientRegEntity clientRegEntity = optionalClientRegEntity.get();
                        clientRegEntity.setTotalVisits(clientRegTotalVisitsDto.getTotalVisits());
                        clientRegRepository.save(clientRegEntity);
                    }
                }
            }
            return clientRegTotalVisitsDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ClientLifeTimeValueDto> calculateClientLifeTimeValue() {
        try {
            List<ClientLifeTimeValueDto> clientLifeTimeValueDtoList = clientRegRepository.getClientLifetimeValue();
            // clientLifeTimeValueDto can be names as anything but ClientLifeTimeValueDto
            // must match the type stored inside the list
            for (ClientLifeTimeValueDto clientLifeTimeValueDto : clientLifeTimeValueDtoList) {
                Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository
                        .findById(clientLifeTimeValueDto.getClientId());
                if (!optionalClientRegEntity.isPresent()) {
                    throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
                }
                ClientRegEntity clientRegEntity = optionalClientRegEntity.get();
                clientRegEntity.setLifetimeValue(clientLifeTimeValueDto.getLifetimeValue());
                clientRegRepository.save(clientRegEntity);
            }
            return clientLifeTimeValueDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
