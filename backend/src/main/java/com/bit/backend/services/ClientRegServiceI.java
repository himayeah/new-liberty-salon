package com.bit.backend.services;

import com.bit.backend.dtos.ClientRegDto;

import java.util.List;

public interface ClientRegServiceI {

    ClientRegDto addClientReg(ClientRegDto clientRegDto);

    List<ClientRegDto> getData();
    ClientRegDto updateClientReg(long id, ClientRegDto clientRegDto);
    ClientRegDto deleteClientReg(long id);

}
