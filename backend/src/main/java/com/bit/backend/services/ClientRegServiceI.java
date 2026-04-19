package com.bit.backend.services;

import com.bit.backend.dtos.ClientRegDto;

import java.util.List;

public interface ClientRegServiceI {

    ClientRegDto addClientReg(ClientRegDto clientRegDto);

    List<ClientRegDto> getData();

    ClientRegDto updateClientReg(long id, ClientRegDto clientRegDto);

    ClientRegDto getById(long id);

    ClientRegDto deleteClientReg(long id);

    // Dashboard card (New Clients within last 30 days)
    long countClientRegistrationsLast30Days();
    // For Public Booking Login
    ClientRegDto findByFirstNameAndEmail(String firstName, String email);

}
