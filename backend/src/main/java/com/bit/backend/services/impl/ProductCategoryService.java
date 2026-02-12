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

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public ProductCategoryDto addProductCategory(ProductCategoryDto productCategoryDto) {
        try {
            ProductCategoryEntity productCategoryEntity = productCategoryMapper.toProductCategoryEntity(productCategoryDto);
            ProductCategoryEntity savedItem = productCategoryRepository.save(productCategoryEntity);
            ProductCategoryDto savedDto = productCategoryMapper.toProductCategoryDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new RuntimeException("Request failed with error:" + e);
        }
    }

    @Override
    public List<ProductCategoryDto> getData() {
        try {
            List<ProductCategoryEntity> productCategoryEntityList = productCategoryRepository.findAll();
            List<ProductCategoryDto> productCategoryDtoList = productCategoryMapper.toProductCategoryDtoList(productCategoryEntityList);
            return productCategoryDtoList;
        } catch (Exception e) {
            throw new RuntimeException("Request failed with error:" + e);
        }
    }

    @Override
    public ProductCategoryDto updateProductCategory(long id, ProductCategoryDto productCategoryDto) {
        try {
            Optional<ProductCategoryEntity> optionalProductCategoryEntity = productCategoryRepository.findById(id);
            if (!optionalProductCategoryEntity.isPresent()) {
                throw new AppException("Product Category Does Not Exist", HttpStatus.BAD_REQUEST);
            }

            ProductCategoryEntity newProductCategoryEntity = productCategoryMapper.toProductCategoryEntity(productCategoryDto);
            newProductCategoryEntity.setId(id);
            ProductCategoryEntity productCategoryEntity = productCategoryRepository.save(newProductCategoryEntity);
            ProductCategoryDto productCategoryDtoRes = productCategoryMapper.toProductCategoryDto(productCategoryEntity);
            System.out.println("update Successfully: " + productCategoryDtoRes.getProductCategoryName());
            return productCategoryDtoRes;
        } catch (Exception e) {
            throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ProductCategoryDto deleteProductCategory(long id) {
        try {
            Optional<ProductCategoryEntity> optionalProductCategoryEntity = productCategoryRepository.findById(id);
            if (!optionalProductCategoryEntity.isPresent()) {
                throw new AppException("Product Category Does Not Exist", HttpStatus.BAD_REQUEST);
        }
            productCategoryRepository.deleteById(id);
            return productCategoryMapper.toProductCategoryDto(optionalProductCategoryEntity.get());
        } catch (Exception e) {
            throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

}
