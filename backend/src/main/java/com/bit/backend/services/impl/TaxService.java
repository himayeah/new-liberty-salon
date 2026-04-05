package com.bit.backend.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bit.backend.dtos.TaxDto;
import com.bit.backend.entities.TaxEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.TaxMapper;
import com.bit.backend.repositories.TaxRepository;
import com.bit.backend.services.TaxServiceI;

@Service
public class TaxService implements TaxServiceI {

    private final TaxRepository taxRepository;
    private final TaxMapper taxMapper;

    public TaxService(TaxRepository taxRepository, TaxMapper taxMapper) {
        this.taxRepository = taxRepository;
        this.taxMapper = taxMapper;
    }

    @Override
    public TaxDto addTax(TaxDto taxDto) {
        try {
            TaxEntity taxEntity = taxMapper.toTaxEntity(taxDto);
            TaxEntity savedItem = taxRepository.save(taxEntity);
            TaxDto savedDto = taxMapper.toTaxDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<TaxDto> getData() {
        try {
            List<TaxEntity> taxEntityList = taxRepository.findAll();
            List<TaxDto> taxDtoList = taxMapper.toTaxDtoList(taxEntityList);
            return taxDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public TaxDto updateTax(long id, TaxDto taxDto) {
        try {
            Optional<TaxEntity> optionalTaxEntity = taxRepository.findById(id);
            if (!optionalTaxEntity.isPresent()) {
                throw new AppException("Tax Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            TaxEntity newTaxEntity = taxMapper.toTaxEntity(taxDto);
            newTaxEntity.setId(id);
            TaxEntity taxEntity = taxRepository.save(newTaxEntity);
            TaxDto taxDtoRes = taxMapper.toTaxDto(taxEntity);
            return taxDtoRes;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public TaxDto deleteTax(long id) {
        try {
            Optional<TaxEntity> optionalTaxEntity = taxRepository.findById(id);
            if (!optionalTaxEntity.isPresent()) {
                throw new AppException("Tax Does Not Exist", HttpStatus.BAD_REQUEST);
            }

            taxRepository.deleteById(id);
            return taxMapper.toTaxDto(optionalTaxEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

}
