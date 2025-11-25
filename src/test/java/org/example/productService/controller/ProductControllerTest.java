package org.example.productService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.productService.dto.ProductDTO;
import org.example.productService.security.JwtAuthenticationFilter;
import org.example.productService.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductDTO product1;
    private ProductDTO product2;

    @BeforeEach
    void setUp() {
        product1 = new ProductDTO();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setCreatedAt(LocalDate.of(2023, 5, 20));

        product2 = new ProductDTO();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setCreatedAt(LocalDate.of(2024, 3, 15));
    }

    @Test
    void testGetProductBetweenPeriod() throws Exception {
        List<ProductDTO> products = Arrays.asList(product1, product2);
        when(productService.getProductsBetweenYears(null, null)).thenReturn(products);

        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(products)));

        verify(productService, times(1)).getProductsBetweenYears(null, null);
    }

    @Test
    void testAddProduct_Single() throws Exception {
        when(productService.createProduct(product1)).thenReturn(product1);

        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(product1)));

        verify(productService, times(1)).createProduct(product1);
    }

    @Test
    void testDeleteProduct() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/{id}", id))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(id);
    }
}
