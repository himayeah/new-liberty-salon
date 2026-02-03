package com.bit.backend.services.impl;

import com.bit.backend.repositories.ProductRepository;
import com.bit.backend.services.ProductServiceI;

import org.springframework.stereotype.Service;
import com.bit.backend.mappers.ProductMapper;
import com.bit.backend.dtos.ProductDto;
import com.bit.backend.entities.ProductEntity;

import java.util.List;
import java.util.Optional;
import com.bit.backend.exceptions.AppException;
import org.springframework.http.HttpStatus;

@Service
public class ProductService implements ProductServiceI {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        try {
            ProductEntity productEntity = productMapper.toProductEntity(productDto);
            ProductEntity savedItem = productRepository.save(productEntity);
            ProductDto savedDto = productMapper.toProductDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public List<ProductDto> getData() {
        try {
            List<ProductEntity> productEntityList = productRepository.findAll();
            List<ProductDto> productDtoList = productMapper.toProductDtoList(productEntityList);
            return productDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProductDto updateProduct(long id, ProductDto productDto) {
        try {
            Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);
            if (!optionalProductEntity.isPresent()) {
                throw new AppException("Product Does Not Exist", HttpStatus.BAD_REQUEST);
            }

            ProductEntity newProductEntity = productMapper.toProductEntity(productDto);
            newProductEntity.setId(id);
            ProductEntity productEntity = productRepository.save(newProductEntity);
            ProductDto productDtoRes = productMapper.toProductDto(productEntity);
            System.out.println("update Successfully: " + productDtoRes.getProductName());
            return productDtoRes;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ProductDto deleteProduct(long id) {
        try {
            Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);
            if (!optionalProductEntity.isPresent()) {
                throw new AppException("Product Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            productRepository.deleteById(id);
            return productMapper.toProductDto(optionalProductEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }
}
