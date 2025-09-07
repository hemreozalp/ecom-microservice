package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRequestDTO;
import com.ecommerce.product_service.dto.ProductResponseDTO;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductMapper.toDTO(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return ProductMapper.toDTO(saved);
    }

    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());

        Product updated = productRepository.save(product);
        return ProductMapper.toDTO(updated);
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
}
