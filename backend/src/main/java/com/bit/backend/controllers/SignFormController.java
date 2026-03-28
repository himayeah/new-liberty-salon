package com.bit.backend.controllers;

import com.bit.backend.dtos.FormDemoDto;
import com.bit.backend.dtos.SignFormDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.SignFormServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
public class SignFormController {
    private final SignFormServiceI signFormServiceI;

    public SignFormController(SignFormServiceI signFormServiceI) {
        this.signFormServiceI = signFormServiceI;
    }

    @PostMapping("/sign-form")
    public ResponseEntity<SignFormDto> signForm(@RequestBody SignFormDto signFormDto) {
        try {
            SignFormDto signFormDtoResponse = signFormServiceI.addSignForm(signFormDto);

            String firstName = (signFormDtoResponse.getFirstName() != null)
                    ? signFormDtoResponse.getFirstName()
                    : "unknown";

            return ResponseEntity
                    .created(URI.create("/sign-form/" + firstName))
                    .body(signFormDtoResponse);

        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //For Retrieving data from backend to FrontEnd
    @GetMapping("/sign-form")
    public ResponseEntity<List<SignFormDto>> getData(){


        /* call to Controller -> Service -> Repository
        we require DTO,Entity and Mapper classes for this */


        try {
            List<SignFormDto> signFormDtoList = signFormServiceI.getData();
            return ResponseEntity.ok(signFormDtoList);
        }

        catch(Exception e){
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping ("/sign-form/{id}")
    public ResponseEntity<SignFormDto> updateSignForm(@PathVariable long id, @RequestBody SignFormDto signFormDto) {
        SignFormDto responseSignFormDto = signFormServiceI.updateSignForm(id, signFormDto);
        return ResponseEntity.ok(responseSignFormDto);
    }

    @DeleteMapping("/sign-form/{id}")
    public ResponseEntity<SignFormDto> deleteSignForm(@PathVariable long id) {
     SignFormDto signFormDto = signFormServiceI.deleteSignForm(id);
        return ResponseEntity.ok(signFormDto);
    }
}

