package com.bit.backend.services;

import com.bit.backend.dtos.ClientLifeTimeValueDto;
import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.dtos.ClientRegTotalVisitsDto;

import java.util.List;

public interface ClientRegServiceI {

    ClientRegDto addClientReg(ClientRegDto clientRegDto);

    List<ClientRegDto> getData();

    ClientRegDto updateClientReg(long id, ClientRegDto clientRegDto);

    ClientRegDto getById(long id);

    ClientRegDto deleteClientReg(long id);

    // Dashboard card (New Clients within last 30 days)
    long countClientRegistrationsLast30Days();

    // For Public Booking Login (Legacy: First Name + Email)
    ClientRegDto findByFirstNameAndEmail(String firstName, String email);

    // Checks the login flow status of a given email ("REGISTER", "SET_PASSWORD", "LOGIN")
    String checkEmailStatus(String email);

    // Registers a brand new client with their initial details and password
    ClientRegDto registerClient(ClientRegDto clientRegDto);

    // Sets a password for an existing client who has no password stored yet
    ClientRegDto setClientPassword(String email, String password);

    // Authenticates client login via email + password credentials
    ClientRegDto loginClient(String email, String password);

    // Dispatches a password reset link to the client's email address
    void forgotPassword(String email);

    // Validates a reset token and sets the client's password
    void resetPassword(String token, String password);

    List<ClientRegTotalVisitsDto> calculateClientVisits();

    List<ClientLifeTimeValueDto> calculateClientLifeTimeValue();

}
