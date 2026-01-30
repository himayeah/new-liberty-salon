package com.bit.backend.services.impl;

import com.bit.backend.dtos.ServiceCategoryDto;
import com.bit.backend.entities.ServiceCategoryEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.ServiceCategoryMapper;
import com.bit.backend.repositories.ServiceCategoryRepository;
import com.bit.backend.services.ServiceCategoryServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ServiceCategoryService implements ServiceCategoryServiceI {

    private final ServiceCategoryRepository serviceCategoryRepository;
    private final ServiceCategoryMapper serviceCategoryMapper;

    public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository, ServiceCategoryMapper serviceCategoryMapper) {
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.serviceCategoryMapper = serviceCategoryMapper;
    }

    @Override
    //Post Method
    public ServiceCategoryDto addServiceCategory(ServiceCategoryDto serviceCategoryDto) {
        try {
            ServiceCategoryEntity serviceCategoryEntity = serviceCategoryMapper.toServiceCategoryEntity(serviceCategoryDto);
            ServiceCategoryEntity savedItem = serviceCategoryRepository.save(serviceCategoryEntity);
            ServiceCategoryDto savedDto = serviceCategoryMapper.toServiceCategoryDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<ServiceCategoryDto> getServiceCategory() {
        try {
            List<ServiceCategoryEntity> serviceCategoryEntityList = serviceCategoryRepository.findAll();
            List<ServiceCategoryDto> serviceCategoryDtoList = serviceCategoryMapper.toServiceCategoryDtoList(serviceCategoryEntityList);
            return serviceCategoryDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }

    }
    @Override
    public ServiceCategoryDto updateServiceCategory(long id, ServiceCategoryDto serviceCategoryDto) {
        try{
            Optional<ServiceCategoryEntity> optionalServiceCategoryEntity = serviceCategoryRepository.findById(id);
            if (!optionalServiceCategoryEntity.isPresent()) {
                throw new AppException("Service Category Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            ServiceCategoryEntity newServiceCategoryEntity = serviceCategoryMapper.toServiceCategoryEntity(serviceCategoryDto);
            ServiceCategoryEntity serviceCategoryEntity = serviceCategoryRepository.save(newServiceCategoryEntity);
            ServiceCategoryDto savedDto = serviceCategoryMapper.toServiceCategoryDto(serviceCategoryEntity);
            return savedDto;
        }
        catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ServiceCategoryDto deleteServiceCategory(long id) {
        try {
            Optional<ServiceCategoryEntity> optionalServiceCategoryEntity = serviceCategoryRepository.findById(id);
            if (!optionalServiceCategoryEntity.isPresent()) {
                throw new AppException("Service Category Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            serviceCategoryRepository.deleteById(id);
            return serviceCategoryMapper.toServiceCategoryDto(optionalServiceCategoryEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }

    }
}
