package com.bit.backend.services.impl;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.repositories.ClientRegRepository;
import com.bit.backend.services.ReportClientRegService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportClientRegServiceSubImpl implements ReportClientRegService {

    private final ClientRegRepository clientRegRepository;
    private final com.bit.backend.mappers.ClientRegMapper clientRegMapper;

    public ReportClientRegServiceSubImpl(ClientRegRepository clientRegRepository, com.bit.backend.mappers.ClientRegMapper clientRegMapper) {
        this.clientRegRepository = clientRegRepository;
        this.clientRegMapper = clientRegMapper;
    }

    @Override
    public List<ClientRegDto> getRegistrationsByYear() {
        try {
            List<Object[]> results = clientRegRepository.countRegistrationsByYear();
            System.out.println("Fetched " + results.size() + " registration year records");
            return results.stream()
                    .map(obj -> {
                        Object[] array = (Object[]) obj;
                        ClientRegDto dto = new ClientRegDto();
                        dto.setRegistrationYear(((Number) array[0]).intValue());
                        dto.setTotalRegistrations(((Number) array[1]).longValue());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching registration data: " + e.getMessage());
            e.printStackTrace();
            throw new com.bit.backend.exceptions.AppException("Error fetching registration data", org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ClientRegDto> getAllClientsData() {
        try {
            List<com.bit.backend.entities.ClientRegEntity> clientRegEntityList = clientRegRepository.findAll();
            return clientRegMapper.toClientRegDtoList(clientRegEntityList);
        } catch (Exception e) {
            throw new com.bit.backend.exceptions.AppException("Request failed with error: " + e.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
