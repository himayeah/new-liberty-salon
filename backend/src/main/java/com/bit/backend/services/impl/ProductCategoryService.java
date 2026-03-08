package com.bit.backend.services.impl;

import com.bit.backend.dtos.ProductCategoryDto;
import com.bit.backend.entities.ProductCategoryEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.ProductCategoryMapper;
import com.bit.backend.repositories.ProductCategoryRepository;
import com.bit.backend.services.ProductCategoryServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService implements ProductCategoryServiceI {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository,
            ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public ProductCategoryDto addProductCategory(ProductCategoryDto productCategoryDto) {
        try {
            ProductCategoryEntity productCategoryEntity = productCategoryMapper
                    .toProductCategoryEntity(productCategoryDto);
            ProductCategoryEntity savedItem = productCategoryRepository.save(productCategoryEntity);
            ProductCategoryDto savedDto = productCategoryMapper.toProductCategoryDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ProductCategoryDto> getData() {
        try {
            List<ProductCategoryEntity> productCategoryEntityList = productCategoryRepository.findAll();
            List<ProductCategoryDto> productCategoryDtoList = productCategoryMapper
                    .toProductCategoryDtoList(productCategoryEntityList);
            return productCategoryDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProductCategoryDto updateProductCategory(long id, ProductCategoryDto productCategoryDto) {
        try {
            Optional<ProductCategoryEntity> optionalProductCategoryEntity = productCategoryRepository.findById(id);
            if (!optionalProductCategoryEntity.isPresent()) {
                throw new AppException("Product Category not found:" + id, HttpStatus.NOT_FOUND);
            }
            ProductCategoryEntity existingEntity = optionalProductCategoryEntity.get();

            if (productCategoryDto.getProductCategoryName() != null) {
                existingEntity.setProductCategoryName(productCategoryDto.getProductCategoryName());
            }

            ProductCategoryEntity savedEntity = productCategoryRepository.save(existingEntity);
            return productCategoryMapper.toProductCategoryDto(savedEntity);
        } catch (Exception e) {
            throw new AppException("Update failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProductCategoryDto deleteProductCategory(long id) {
        try {
            Optional<ProductCategoryEntity> optionalProductCategoryEntity = productCategoryRepository.findById(id);
            if (!optionalProductCategoryEntity.isPresent()) {
                throw new AppException("Product Category Does Not Exist", HttpStatus.NOT_FOUND);
            }
            productCategoryRepository.deleteById(id);
            return productCategoryMapper.toProductCategoryDto(optionalProductCategoryEntity.get());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
