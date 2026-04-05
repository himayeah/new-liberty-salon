package com.bit.backend.services;

import com.bit.backend.dtos.ClientRegDto;
import java.util.List;

public interface ReportClientRegService {
    List<ClientRegDto> getRegistrationsByYear();
    List<ClientRegDto> getAllClientsData();
}
