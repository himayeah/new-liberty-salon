package com.bit.backend.controllers;

import com.bit.backend.dtos.ServiceCategoryDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ServiceCategoryServiceI;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
public class ServiceCategoryController {

    private final ServiceCategoryServiceI serviceCategoryServiceI;

    public ServiceCategoryController(ServiceCategoryServiceI serviceCategoryServiceI) {
        this.serviceCategoryServiceI = serviceCategoryServiceI;
    }

    @PostMapping
    public ResponseEntity<ServiceCategoryDto> addForm(@RequestBody ServiceCategoryDto serviceCategoryDto) throws AppException {
        try{
            ServiceCategoryDto serviceCategoryResponse = serviceCategoryServiceI.addServiceCategory(serviceCategoryDto);
            return ResponseEntity.created(URI.create("/service-category/" + serviceCategoryResponse.getServiceName())).body(serviceCategoryResponse);
            } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ServiceCategoryDto>> getServiceCategory(){
        try {
            List<ServiceCategoryDto> serviceCategoryDtoList = serviceCategoryServiceI.getServiceCategory();
            return  ResponseEntity.ok().body(serviceCategoryDtoList);
        }
        catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategoryDto> updateServiceCategory(
            @PathVariable long id,
            @RequestBody ServiceCategoryDto serviceCategoryDto) {
        try{
            ServiceCategoryDto responseServiceCategoryDt = serviceCategoryServiceI.updateServiceCategory(id, serviceCategoryDto);
            return ResponseEntity.ok(responseServiceCategoryDt);
        }
        catch (Exception e) {
            throw  new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceCategoryDto> deleteServiceCategory(@PathVariable long id) {
        try{
            ServiceCategoryDto serviceCategoryDto = serviceCategoryServiceI.deleteServiceCategory(id);
            return ResponseEntity.ok(serviceCategoryDto);
        }
        catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
