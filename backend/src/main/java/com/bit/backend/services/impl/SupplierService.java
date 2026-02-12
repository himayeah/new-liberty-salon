package com.bit.backend.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import com.bit.backend.exceptions.AppException;
import com.bit.backend.repositories.SupplierRepository;
import com.bit.backend.services.SupplierServiceI;
import com.bit.backend.mappers.SupplierMapper;
import com.bit.backend.entities.SupplierEntity;
import com.bit.backend.dtos.SupplierDto;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService implements SupplierServiceI {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierMapper = supplierMapper;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierDto addSupplier(SupplierDto supplierDto) {
        try {
            SupplierEntity supplierEntity = supplierMapper.toSupplierEntity(supplierDto);
            SupplierEntity savedItem = supplierRepository.save(supplierEntity);
            SupplierDto savedDto = supplierMapper.toSupplierDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<SupplierDto> getSupplier() {
        try {
            List<SupplierEntity> supplierEntityList = supplierRepository.findAll();
            List<SupplierDto> supplierDtoList = supplierMapper.toSupplierDtoList(supplierEntityList);
            return supplierDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public SupplierDto updateSupplier(long id, SupplierDto supplierDto) {
        try {
            Optional<SupplierEntity> optionalSupplierEntity = supplierRepository.findById(id);
            if (!optionalSupplierEntity.isPresent()) {
                throw new AppException("Supplier Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            SupplierEntity newSupplierEntity = supplierMapper.toSupplierEntity(supplierDto);
            newSupplierEntity.setId(id);
            SupplierEntity supplierEntity = supplierRepository.save(newSupplierEntity);
            SupplierDto responseDto = supplierMapper.toSupplierDto(supplierEntity);
            return responseDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public SupplierDto deleteSupplier(long id) {
        try {
            Optional<SupplierEntity> optionalSupplierEntity = supplierRepository.findById(id);
            if (!optionalSupplierEntity.isPresent()) {
                throw new AppException("Supplier Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            supplierRepository.deleteById(id);
            return supplierMapper.toSupplierDto(optionalSupplierEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

}
