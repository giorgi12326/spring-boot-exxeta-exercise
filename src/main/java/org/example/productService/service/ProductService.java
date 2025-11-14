package org.example.productService.service;

import org.example.productService.dto.EventType;
import org.example.productService.dto.ProductDTO;
import org.example.productService.dto.Event;
import org.example.productService.entity.Product;
import org.example.productService.exception.ResourceNotFoundException;
import org.example.productService.mapper.ProductMapper;
import org.example.productService.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, Event> kafkaTemplate;

    public List<ProductDTO> getProducts() {
        List<Product> all = productRepository.findAll();
        return productMapper.toDTOs(all);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product entity = productMapper.toEntity(productDTO);
        Product save = productRepository.save(entity);
        return productMapper.toDTO(save);
    }

    @Transactional
    public List<ProductDTO> createProducts(List<ProductDTO> productDTOs) {
        List<Product> entities = productMapper.toEntities(productDTOs);
        List<Product> products = productRepository.saveAll(entities);
        return productMapper.toDTOs(products);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        Event event = new Event(EventType.DELETED, Instant.now(),id);
        kafkaTemplate.send("product-event", event);
    }

    public List<ProductDTO> getProductsBetweenYears(Integer startYear, Integer endYear) {
        if(startYear == null && endYear == null) {
            return productMapper.toDTOs(productRepository.findAll());
        }
        if(endYear == null) {
            LocalDate start = LocalDate.of(startYear-1, 12, 31);
            return  productMapper.toDTOs(productRepository.findByCreatedAtAfter(start));
        }
        if(startYear == null) {
            LocalDate end = LocalDate.of(endYear+1, 1, 1);
            return  productMapper.toDTOs(productRepository.findByCreatedAtBefore(end));
        }
        LocalDate start = LocalDate.of(startYear-1, 12, 31);
        LocalDate end = LocalDate.of(endYear+1, 1, 1);
        List<Product> products = productRepository.findByCreatedAtBetween(start, end);
        return productMapper.toDTOs(products);
    }

    public ProductDTO getProductById(Long id) {
        Product byId = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("productNotFound"));
        return productMapper.toDTO(byId);
    }

    public boolean existsById(Long id) {
        System.out.println("asd");
        return productRepository.existsById(id);
    }
}