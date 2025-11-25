package org.example.productService.service;

import org.example.productService.dto.*;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, Event> kafkaTemplate;


    public ProductDTO getProductById(Long id) {
        Product byId = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("productNotFound"));
        return productMapper.toDTO(byId);
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

    @Transactional
    public List<ReserveResponseDTO> getAndReserveProducts(List<ReserveProductDTO> reserveProductDTO) {
        List<Product> dtos = new ArrayList<>();
        for(ReserveProductDTO productDTO : reserveProductDTO) {
            Product product = productRepository.findProductById(productDTO.getProductId()).orElseThrow(() -> new ResourceNotFoundException("product Not Found with ID: " + productDTO.getProductId()));
            if(product.getQuantity()-productDTO.getQuantity() >= 0)
                product.setQuantity(product.getQuantity()-productDTO.getQuantity());
            else
                throw new IllegalStateException("Not enough stock for product " + product.getId());

            dtos.add(product);
        }
        List<Product> products = productRepository.saveAll(dtos);

        return productMapper.toReserveResponses(products);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product entity = productMapper.toEntity(productDTO);
        Product save = productRepository.save(entity);
        return productMapper.toDTO(save);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product Not Found with ID: " + id));
        productRepository.delete(product);
        Event event = new Event(EventType.DELETED, Instant.now(),id);
        kafkaTemplate.send("product-event", event);
    }
}