package com.bit.backend.controllers;

import com.bit.backend.dtos.ServiceDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ServiceServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceServiceI serviceServiceI;

    public ServiceController(ServiceServiceI serviceserviceI) {
        this.serviceServiceI = serviceserviceI;
    }

    @PostMapping
    public ResponseEntity<ServiceDto> addService(@RequestBody ServiceDto serviceDto) throws AppException {

        try {
            ServiceDto serviceDtoResponse = serviceServiceI.addService(serviceDto);
            return ResponseEntity.created(URI.create("/service" + serviceDtoResponse.getServiceName())).body(serviceDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ServiceDto>> getService(){
       try{
           List<ServiceDto> serviceDtoList = serviceServiceI.getService();
           return ResponseEntity.ok(serviceDtoList);
       } catch (Exception e) {
           throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

//    @PutMapping ("/service-edit/${id}")
//    public ResponseEntity<ServiceDto> updateService(@PathVariable long id, @RequestBody ServiceDto serviceDto) {
//        try {
//            ServiceDto responseServiceDto = serviceServiceI.updateService(id, serviceDto);
//            return ResponseEntity.ok(responseServiceDto);
//        } catch (Exception e) {
//            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceDto> deleteService(@PathVariable long id) {
        try {
            ServiceDto responseServiceDto = serviceServiceI.deleteService(id);
            return ResponseEntity.ok(responseServiceDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
