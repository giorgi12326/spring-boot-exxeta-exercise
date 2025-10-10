package exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exercise.dto.ProductDTO;
import exercise.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
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
    void testAddProduct_Batch() throws Exception {
        List<ProductDTO> products = Arrays.asList(product1, product2);
        when(productService.createProducts(products)).thenReturn(products);

        mockMvc.perform(post("/api/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(products)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(products)));

        verify(productService, times(1)).createProducts(products);
    }

    @Test
    void testDeleteProduct() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/{id}", id))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(id);
    }
}
