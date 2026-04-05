package com.bit.backend.controllers;

import com.bit.backend.dtos.StaffServicesDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.StaffServicesServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/staff-services")
public class StaffServicesController {

    private final StaffServicesServiceI staffServicesServiceI;

    public StaffServicesController(StaffServicesServiceI staffServicesServiceI) {
        this.staffServicesServiceI = staffServicesServiceI;
    }

    @PostMapping
    public ResponseEntity<StaffServicesDto> createStaffServices(@RequestBody StaffServicesDto staffServicesDto) throws AppException {
        try{
            StaffServicesDto staffServicesDtoResponse = staffServicesServiceI.createStaffServices(staffServicesDto);
            return ResponseEntity.created(URI.create("/staff-services" + staffServicesDtoResponse.getServiceName())).body(staffServicesDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<StaffServicesDto>> getStaffServices(){
        try{
            List<StaffServicesDto> staffServicesDtoList = staffServicesServiceI.getStaffServices();
            return ResponseEntity.ok(staffServicesDtoList);
        }catch (Exception e){
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffServicesDto> updateStaffServices(
            @PathVariable Long id,
            @RequestBody StaffServicesDto staffServicesDto
    ){
        try{
            StaffServicesDto responseStaffServicesDto = staffServicesServiceI.updateStaffServices(id, staffServicesDto);
            return ResponseEntity.ok(responseStaffServicesDto);
        }
        catch (Exception e){
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StaffServicesDto> deleteStaffServices(@PathVariable Long id) {
        try {
            StaffServicesDto staffServicesDto =
                    staffServicesServiceI.deleteStaffServices(id);

            return ResponseEntity.ok(staffServicesDto);

        } catch (Exception e) {
            throw new AppException(
                    "Request failed with error: " + e,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


}

