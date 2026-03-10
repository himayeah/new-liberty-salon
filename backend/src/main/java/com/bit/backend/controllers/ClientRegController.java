package com.bit.backend.controllers;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.dtos.ClientRegReportDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ClientRegServiceI;
import com.bit.backend.services.ReportClientRegService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/client-reg") // Base path for all client-reg endpoints
public class ClientRegController {

    private final ClientRegServiceI clientRegServiceI;
    private final ReportClientRegService reportClientRegService;

    public ClientRegController(ClientRegServiceI clientRegServiceI, ReportClientRegService reportClientRegService) {
        this.clientRegServiceI = clientRegServiceI;
        this.reportClientRegService = reportClientRegService;
    }

    // ResponseEntity<ClientRegDto> : The response body will contain a ClientRegDto
    // ResponseEntity is a wrapper Spring uses to send a response back to the
    // frontend.
    @PostMapping
    public ResponseEntity<ClientRegDto> addForm(@RequestBody ClientRegDto clientRegDto) throws AppException {

        try {
            System.out.println("Data Object :" + clientRegDto);
            System.out.println("Client First Name  :" + clientRegDto.getFirstName());

            // send the DTO to the addClientReg() method in the service interface class, and
            // do the function accordingly,
            // then save the response as the clientRegDtoResponse (Which is a ClientRegDto
            // type)
            ClientRegDto clientRegDtoResponse = clientRegServiceI.addClientReg(clientRegDto);
            return ResponseEntity.created(URI.create("/api/v1/client-reg/" + clientRegDtoResponse.getId()))
                    .body(clientRegDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClientRegDto>> getData() {

        try {
            List<ClientRegDto> clientRegDtoList = clientRegServiceI.getData();
            return ResponseEntity.ok(clientRegDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/registrations")
    public ResponseEntity<List<ClientRegReportDto>> getRegistrationsByYear() {
        try {
            List<ClientRegReportDto> reportData = reportClientRegService.getRegistrationsByYear();
            return ResponseEntity.ok(reportData);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientRegDto> getById(@PathVariable long id) {
        try {
            ClientRegDto clientRegDto = clientRegServiceI.getById(id);
            return ResponseEntity.ok(clientRegDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientRegDto> updateClientReg(
            @PathVariable long id,
            @RequestBody ClientRegDto clientRegDto) {
        try {
            System.out.println("Data Received");
            ClientRegDto responseClientRegDto = clientRegServiceI.updateClientReg(id, clientRegDto);
            return ResponseEntity.ok(responseClientRegDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientRegDto> deleteClientReg(@PathVariable long id) {

        try {
            // Defines a method that takes the id from the URL path and returns a
            // ResponseEntity containing a ClientRegDto object.
            ClientRegDto clientRegDto = clientRegServiceI.deleteClientReg(id);
            // Calls a service method to delete the client with the given id, and stores the
            // deleted client's data in clientRegDto.
            return ResponseEntity.ok(clientRegDto);
            // Returns an HTTP 200 OK response with the deleted client's data in the
            // response body.
            // Next Step: Go to ClientRegServiceI
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

