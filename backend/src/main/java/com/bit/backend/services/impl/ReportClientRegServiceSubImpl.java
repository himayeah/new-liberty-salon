package com.bit.backend.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.repositories.ClientRegRepository;
import com.bit.backend.services.ReportClientRegService;

@Service
public class ReportClientRegServiceSubImpl implements ReportClientRegService {

    private final ClientRegRepository clientRegRepository;
    private final com.bit.backend.mappers.ClientRegMapper clientRegMapper;

    public ReportClientRegServiceSubImpl(ClientRegRepository clientRegRepository,
            com.bit.backend.mappers.ClientRegMapper clientRegMapper) {
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
            throw new com.bit.backend.exceptions.AppException("Error fetching registration data",
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ClientRegDto> getAllClientsData() {
        try {
            List<com.bit.backend.entities.ClientRegEntity> clientRegEntityList = clientRegRepository.findAll();
            return clientRegMapper.toClientRegDtoList(clientRegEntityList);
        } catch (Exception e) {
            throw new com.bit.backend.exceptions.AppException("Request failed with error: " + e.getMessage(),
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Registrations by Gender
    @Override
    public List<ClientRegDto> getRegistrationsByGender() {
        try {
            // Example 'results' Object array returned from the query
            // results = [
            // ["January", "Male", 25],
            // ["January", "Female", 30],
            // ["February", "Male", 20]
            // ];
            List<Object[]> results = clientRegRepository.getRegistrationsByGender();
            System.out.println("Fetched " + results.size() + " registration records");
            // You stream the entire clientRegDto result and map them to the only columns
            // you need in thr frontend Table
            // Stream can be used only once, you can't reuse a stream
            // ( You can only consume a stream once )
            //
            return results.stream()
                    // NOTE
                    // ------------------------------------------------------------
                    // .stream().sorted() will sort the array values
                    // .map() gives you a new stream with all the array values and you can tell it
                    // what operation you want to do on each value
                    // Ex 1: numArray.stream().map(num -> num * 2).printlnt(data);
                    // Ex 2: numArray.stream() --> stream 1
                    // .filter(num -> num%2 == 0) ---> stream 2
                    // .sorted() ---> stream 3
                    // .map(num -> num * 2) ---> stream 4 ( You have 3 different streams)

                    .map(obj -> {
                        Object[] array = (Object[]) obj;
                        ClientRegDto dto = new ClientRegDto();
                        // Manual object mapping to what the Repo query returns (RegistrationMonth,
                        // Gender, TotalRegistrations)
                        // SQL query returns;
                        // ["January", "Male", 25]
                        // ["January", "Female", 30]
                        // After object mapping, Each row becomes:
                        // Object[] = [month, gender, total]

                        // So index positions mean:

                        // array[0] → registrationMonth
                        // array[1] → gender
                        // array[2] → totalRegistrations
                        dto.setRegistrationYear(((Number) array[0]).intValue());
                        dto.setRegistrationMonth(array[1] != null ? array[1].toString() : null);
                        dto.setTotalMaleRegistrations(((Number) array[2]).longValue());
                        dto.setTotalFemaleRegistrations(((Number) array[3]).longValue());
                        System.out.println("Registrations By Gender:" + dto);
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching registration data: " + e.getMessage());
            e.printStackTrace();
            throw new com.bit.backend.exceptions.AppException("Error fetching registration data",
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Client Registrations By Age Group (Pie Chart)
    @Override
    public List<ClientRegDto> getRegistrationsByAgeGroup(String startDate, String endDate) {
        try {
            String start = (startDate == null || startDate.trim().isEmpty()) ? "1970-01-01" : startDate;
            String end = (endDate == null || endDate.trim().isEmpty()) ? "9999-12-31" : endDate;

            List<Object[]> results = clientRegRepository.getRegistrationsByAgeGroup(start, end);

            // Accessing Object Array elements
            for (Object[] row : results) {
                System.out.println("Age Group: " + row[0]);
                System.out.println("----------------------");
                System.out.println("Total Client Count: " + row[1]);
            }

            return results.stream()
                    .map(obj -> {
                        Object[] array = (Object[]) obj;
                        ClientRegDto dto = new ClientRegDto();
                        dto.setClientAgeGroup(array[0] != null ? array[0].toString() : null);
                        dto.setTotalClientCount(((Number) array[1]).intValue());
                        System.out.println("Registrations By Age Group:" + dto);
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching registration data: " + e.getMessage());
            e.printStackTrace();
            throw new com.bit.backend.exceptions.AppException("Error fetching registration data",
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
