package com.bit.backend.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.dtos.ClientRegTotalVisitsDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ClientRegServiceI;
import com.bit.backend.services.ReportClientRegService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/v1/client-reg") // Base path for all client-reg endpoints
public class ClientRegController {

    private final ClientRegServiceI clientRegServiceI;
    private final ReportClientRegService reportClientRegService;

    public ClientRegController(ClientRegServiceI clientRegServiceI, ReportClientRegService reportClientRegService) {
        this.clientRegServiceI = clientRegServiceI;
        this.reportClientRegService = reportClientRegService;
    }

    // ResponseEntity<ClientRegDto> : The final response body that's sent to the
    // frontend will contain a ClientRegDto
    // ResponseEntity is a wrapper Spring uses to send a response back to the
    // frontend
    // addForm(@RequestBody ClientRegDto clientRegDto): Take the JSON sent from the
    // frontend request body and Spring converts it into a Java object( the DTO,
    // then sends this DTO data to the addClientReg() method in the service
    // interface
    @PostMapping
    public ResponseEntity<ClientRegDto> addForm(@RequestBody ClientRegDto clientRegDto) throws AppException {

        try {
            System.out.println("Data Object :" + clientRegDto);
            System.out.println("Client First Name  :" + clientRegDto.getFirstName());

            // send the DTO to the addClientReg() method in the service interface class, and
            // do the function accordingly,
            // then save the response returned from the ServiceI as the clientRegDtoResponse
            // (Which is a ClientRegDto
            // type)
            // @RequestBody → reads the JSON body and converts it into a Java object (your
            // ClientRegDto)
            // @ResponseBody → convert the Java object → JSON
            // HttpMessageConverter (usually Jackson) converts JSON -> DTO and vice versa
            ClientRegDto clientRegDtoResponse = clientRegServiceI.addClientReg(clientRegDto);
            // the final responseEntity is returned it contains:
            // → URI.create("/api/v1/client-reg/" + clientRegDtoResponse.getId()) → URL of
            // the newly created resource(201 status code)
            // → .body(clientRegDtoResponse) → response body(the ClientRegDto object)
            return ResponseEntity.created(URI.create("/api/v1/client-reg/" + clientRegDtoResponse.getId()))
                    .body(clientRegDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientRegDto>> getData() {

        try {
            List<ClientRegDto> clientRegDtoList = clientRegServiceI.getData();
            return ResponseEntity.ok(clientRegDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // calculate Total client visits
    @PutMapping("/calculate-total-visits")
    public ResponseEntity<List<ClientRegTotalVisitsDto>> calculateClientVisits() throws AppException {
        try {
            List<ClientRegTotalVisitsDto> updatedClients = clientRegServiceI.calculateClientVisits();
            return ResponseEntity.ok(updatedClients);
        } catch (Exception e) {
            throw new AppException(
                    "Request failed with error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET CLIENT REGISTRATIONS FOR THE REPORT
    @GetMapping("/registrations")
    public ResponseEntity<List<ClientRegDto>> getRegistrationsByYear() {
        try {
            List<ClientRegDto> reportData = reportClientRegService.getRegistrationsByYear();
            return ResponseEntity.ok(reportData);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Dashboard card (New Clients within last 30 days)
    @GetMapping("/count-last-30-days")
    public ResponseEntity<Long> countClientRegistrationsLast30Days() {
        try {
            long count = clientRegServiceI.countClientRegistrationsLast30Days();
            return ResponseEntity.ok(count);
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

    // Search Client for Public Booking Login
    @GetMapping("/search")
    public ResponseEntity<ClientRegDto> searchClient(@RequestParam String firstName, @RequestParam String email) {
        try {
            ClientRegDto clientRegDto = clientRegServiceI.findByFirstNameAndEmail(firstName, email);
            if (clientRegDto != null) {
                return ResponseEntity.ok(clientRegDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
