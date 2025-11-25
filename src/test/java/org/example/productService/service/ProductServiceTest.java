package org.example.productService.service;

import org.example.productService.dto.ProductDTO;
import org.example.productService.entity.Product;
import org.example.productService.mapper.ProductMapper;
import org.example.productService.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    private Product product1;
    private Product product2;
    private ProductDTO dto1;
    private ProductDTO dto2;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, productMapper,null);

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setCreatedAt(LocalDate.of(2023,5,20));

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setCreatedAt(LocalDate.of(2024,3,15));

        dto1 = new ProductDTO();
        dto1.setName("Product 1");
        dto1.setDescription("Description 1");
        dto1.setCreatedAt(LocalDate.of(2023,5,20));

        dto2 = new ProductDTO();
        dto2.setName("Product 2");
        dto2.setDescription("Description 2");
        dto2.setCreatedAt(LocalDate.of(2024,3,15));
    }


    @Test
    void testCreateProduct() {
        when(productMapper.toEntity(dto1)).thenReturn(product1);
        when(productRepository.save(product1)).thenReturn(product1);
        when(productMapper.toDTO(product1)).thenReturn(dto1);

        ProductDTO result = productService.createProduct(dto1);

        assertEquals(dto1, result);
        verify(productMapper).toEntity(dto1);
        verify(productRepository).save(product1);
        verify(productMapper).toDTO(product1);
    }

    @Test
    void testDeleteProduct() {
        Long id = 1L;

        productService.deleteProduct(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetProductsBetweenYears_BothNull() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(productMapper.toDTOs(Arrays.asList(product1, product2))).thenReturn(Arrays.asList(dto1, dto2));

        List<ProductDTO> result = productService.getProductsBetweenYears(null, null);

        assertEquals(Arrays.asList(dto1, dto2), result);
        verify(productRepository).findAll();
        verify(productMapper).toDTOs(Arrays.asList(product1, product2));
    }

    @Test
    void testGetProductsBetweenYears_StartYearOnly() {
        when(productRepository.findByCreatedAtAfter(LocalDate.of(2022,12,31)))
                .thenReturn(Arrays.asList(product1));
        when(productMapper.toDTOs(Arrays.asList(product1)))
                .thenReturn(Arrays.asList(dto1));

        List<ProductDTO> result = productService.getProductsBetweenYears(2023, null);

        assertEquals(Arrays.asList(dto1), result);
        verify(productRepository).findByCreatedAtAfter(LocalDate.of(2022,12,31));
        verify(productMapper).toDTOs(Arrays.asList(product1));
    }

    @Test
    void testGetProductsBetweenYears_EndYearOnly() {
        when(productRepository.findByCreatedAtBefore(LocalDate.of(2025,1,1)))
                .thenReturn(Arrays.asList(product1, product2));
        when(productMapper.toDTOs(Arrays.asList(product1, product2)))
                .thenReturn(Arrays.asList(dto1, dto2));

        List<ProductDTO> result = productService.getProductsBetweenYears(null, 2024);

        assertEquals(Arrays.asList(dto1, dto2), result);
        verify(productRepository).findByCreatedAtBefore(LocalDate.of(2025,1,1));
        verify(productMapper).toDTOs(Arrays.asList(product1, product2));
    }

    @Test
    void testGetProductsBetweenYears_BothYears() {
        when(productRepository.findByCreatedAtBetween(LocalDate.of(2022,12,31), LocalDate.of(2025,1,1)))
                .thenReturn(Arrays.asList(product1, product2));
        when(productMapper.toDTOs(Arrays.asList(product1, product2)))
                .thenReturn(Arrays.asList(dto1, dto2));

        List<ProductDTO> result = productService.getProductsBetweenYears(2023, 2024);

        assertEquals(Arrays.asList(dto1, dto2), result);
        verify(productRepository).findByCreatedAtBetween(LocalDate.of(2022,12,31), LocalDate.of(2025,1,1));
        verify(productMapper).toDTOs(Arrays.asList(product1, product2));
    }
}
