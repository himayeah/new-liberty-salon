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
        if (supplierDto == null) {
            throw new AppException("Supplier data cannot be null", HttpStatus.BAD_REQUEST);
        }
        try {
            supplierDto.setId(null); // Ensure creation
            SupplierEntity supplierEntity = supplierMapper.toSupplierEntity(supplierDto);
            SupplierEntity savedItem = supplierRepository.save(supplierEntity);
            return supplierMapper.toSupplierDto(savedItem);
        } catch (Exception e) {
            throw new AppException("Failed to add supplier: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<SupplierDto> getSupplier() {
        try {
            List<SupplierEntity> supplierEntityList = supplierRepository.findAll();
            return supplierMapper.toSupplierDtoList(supplierEntityList);
        } catch (Exception e) {
            throw new AppException("Failed to retrieve suppliers: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public SupplierDto updateSupplier(long id, SupplierDto supplierDto) {
        if (supplierDto == null) {
            throw new AppException("Supplier data cannot be null", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!supplierRepository.existsById(id)) {
                throw new AppException("Supplier with ID " + id + " not found", HttpStatus.NOT_FOUND);
            }

            supplierDto.setId(id);
            SupplierEntity supplierEntityToUpdate = supplierMapper.toSupplierEntity(supplierDto);
            SupplierEntity updatedEntity = supplierRepository.save(supplierEntityToUpdate);
            return supplierMapper.toSupplierDto(updatedEntity);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to update supplier: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public SupplierDto deleteSupplier(long id) {
        try {
            SupplierEntity supplierEntity = supplierRepository.findById(id)
                    .orElseThrow(() -> new AppException("Supplier with ID " + id + " not found", HttpStatus.NOT_FOUND));

            supplierRepository.deleteById(id);
            return supplierMapper.toSupplierDto(supplierEntity);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to delete supplier: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
