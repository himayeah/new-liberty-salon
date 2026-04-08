package com.bit.backend.services.impl;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bit.backend.dtos.BillingDto;
import com.bit.backend.entities.BillingEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.BillingMapper;
import com.bit.backend.repositories.BillingRepository;
import com.bit.backend.services.BillingService;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;
    private final BillingMapper billingMapper;

    public BillingServiceImpl(BillingRepository billingRepository, BillingMapper billingMapper){
        this.billingMapper = billingMapper;
        this.billingRepository = billingRepository;
    }

    @Override
    public BillingDto addBilling(BillingDto dto) {
        try {
            BillingEntity entity = billingMapper.toBillingEntity(dto);
            if (entity.getPurchases() != null) {
                entity.getPurchases().forEach(p -> p.setBilling(entity));
            }
            BillingEntity savedItem = billingRepository.save(entity);
            return billingMapper.toBillingDto(savedItem);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public java.util.List<BillingDto> getBilling() {
        try {
            java.util.List<BillingEntity> entities = billingRepository.findAll();
            return billingMapper.toBillingDtoList(entities);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BillingDto updateBilling(long id, BillingDto dto) {
        try {
            if (!billingRepository.existsById(id)) {
                throw new AppException("Billing not found with id: " + id, HttpStatus.NOT_FOUND);
            }
            BillingEntity entity = billingMapper.toBillingEntity(dto);
            entity.setId(id);
            if (entity.getPurchases() != null) {
                entity.getPurchases().forEach(p -> p.setBilling(entity));
            }
            BillingEntity updatedItem = billingRepository.save(entity);
            return billingMapper.toBillingDto(updatedItem);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Update failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BillingDto deleteBilling(long id) {
        try {
            java.util.Optional<BillingEntity> entityOpt = billingRepository.findById(id);
            if (entityOpt.isPresent()) {
                billingRepository.delete(entityOpt.get());
                return billingMapper.toBillingDto(entityOpt.get());
            } else {
                throw new AppException("Billing not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e, HttpStatus.BAD_REQUEST);
        }
    }
}
