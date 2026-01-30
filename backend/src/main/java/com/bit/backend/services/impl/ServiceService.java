package com.bit.backend.services.impl;

import com.bit.backend.dtos.ServiceDto;
import com.bit.backend.entities.ServiceEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.ServiceMapper;
import com.bit.backend.repositories.ServiceRepository;
import com.bit.backend.services.ServiceServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService implements ServiceServiceI {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    public ServiceService(ServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public ServiceDto addService(ServiceDto serviceDto) {
        try{
            ServiceEntity serviceEntity = serviceMapper.toServiceEntity(serviceDto);
            ServiceEntity savedItem = serviceRepository.save(serviceEntity);
            ServiceDto savedDto = serviceMapper.toServiceDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ServiceDto> getService() {
        try {
            List<ServiceEntity> serviceEntityList = serviceRepository.findAll();
            List<ServiceDto> serviceDtoList = serviceMapper.toServiceDtoList(serviceEntityList);
            return serviceDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ServiceDto updateService(long id, ServiceDto serviceDto) {

        try{
            Optional<ServiceEntity> optionalServiceEntity = serviceRepository.findById(id);
            if (!optionalServiceEntity.isPresent()) {
                throw new AppException("Request failed with error:" + HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
            }
            ServiceEntity newServiceEntity = serviceMapper.toServiceEntity(serviceDto);
            ServiceEntity serviceEntity = serviceRepository.save(newServiceEntity);
            ServiceDto responseServiceDto = serviceMapper.toServiceDto(serviceEntity);
            return responseServiceDto;
        }
        catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ServiceDto deleteService(long id) {
        try {
            Optional<ServiceEntity> optionalServiceEntity = serviceRepository.findById(id);
            if(!optionalServiceEntity.isPresent()){
                throw new AppException("Request failed with error:" + HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
            }
            serviceRepository.deleteById(id);
            return serviceMapper.toServiceDto(optionalServiceEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
