package com.bit.backend.services.impl;

import com.bit.backend.repositories.ProductRepository;
import com.bit.backend.services.ProductServiceI;

import org.springframework.stereotype.Service;
import com.bit.backend.mappers.ProductMapper;
import com.bit.backend.dtos.ProductDto;
import com.bit.backend.entities.ProductEntity;

import java.util.List;
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
        if (productDto == null) {
            throw new AppException("Product data cannot be null", HttpStatus.BAD_REQUEST);
        }
        try {
            // Ensure ID is null for a fresh save
            productDto.setId(null);
            ProductEntity productEntity = productMapper.toProductEntity(productDto);
            ProductEntity savedItem = productRepository.save(productEntity);
            return productMapper.toProductDto(savedItem);
        } catch (Exception e) {
            throw new AppException("Failed to add product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ProductDto> getData() {
        try {
            List<ProductEntity> productEntityList = productRepository.findAll();
            return productMapper.toProductDtoList(productEntityList);
        } catch (Exception e) {
            throw new AppException("Failed to retrieve products: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProductDto updateProduct(long id, ProductDto productDto) {
        if (productDto == null) {
            throw new AppException("Product data cannot be null", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!productRepository.existsById(id)) {
                throw new AppException("Product with ID " + id + " not found", HttpStatus.NOT_FOUND);
            }

            productDto.setId(id);
            ProductEntity productEntityToUpdate = productMapper.toProductEntity(productDto);
            ProductEntity updatedEntity = productRepository.save(productEntityToUpdate);
            System.out.println("Product updated successfully: " + updatedEntity.getProductName());
            return productMapper.toProductDto(updatedEntity);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to update product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProductDto deleteProduct(long id) {
        try {
            ProductEntity productEntity = productRepository.findById(id)
                    .orElseThrow(() -> new AppException("Product with ID " + id + " not found", HttpStatus.NOT_FOUND));

            productRepository.deleteById(id);
            return productMapper.toProductDto(productEntity);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to delete product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
