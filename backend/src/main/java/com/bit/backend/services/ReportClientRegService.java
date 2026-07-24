package com.bit.backend.services;

import java.util.List;

import com.bit.backend.dtos.ClientRegDto;

public interface ReportClientRegService {
    List<ClientRegDto> getRegistrationsByYear();

    List<ClientRegDto> getAllClientsData();

    List<ClientRegDto> getRegistrationsByGender();

    List<ClientRegDto> getRegistrationsByAgeGroup(String startDate, String endDate);

}
