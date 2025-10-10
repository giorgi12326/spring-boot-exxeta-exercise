package exercise.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.dto.ProductDTO;
import exercise.entity.Product;
import exercise.repository.ProductRepository;
import exercise.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductIntegrationTest {

    private ProductDTO postcreaterDTO;
    private Product product;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("yourpassword");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @DynamicPropertySource
    static void registerManualDb(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> "jdbc:postgresql://localhost:5432/mypg");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "yourpassword");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO getCheckerDTO;


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        getCheckerDTO = ProductDTO.builder()
                .name("Product Name")
                .description("product Description")
                .createdAt(LocalDate.now())
                .build();

        postcreaterDTO = ProductDTO.builder()
                .name("Product Name1")
                .description("product Description1")
                .createdAt(LocalDate.now())
                .build();

        product = Product.builder()
                .name("Product Name")
                .description("product Description")
                .createdAt(LocalDate.now())
                .build();


        productRepository.save(product);
    }

    @Test
    void testGetProductBetweenPeriod() throws Exception {
        String contentAsString = mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {});
        assertThat(productDTO).contains(getCheckerDTO);

    }
    @Test
    void testGetProductBetweenPeriodWithAfterFound() throws Exception {
        String contentAsString = mockMvc.perform(get("/api")
                        .param("startYear", "2021"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {});
        assertThat(productDTO).contains(getCheckerDTO);

    }

    @Test
    void testGetProductBetweenPeriodWithAfterNotFound() throws Exception {
        String contentAsString = mockMvc.perform(get("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCheckerDTO))
                .param("startYear", "2026"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {});
        assertThat(productDTO).doesNotContain(getCheckerDTO);
    }

    @Test
    void testGetProductBetweenPeriodWithBeforeFound() throws Exception {
        String contentAsString = mockMvc.perform(get("/api")
                        .param("endYear", "2026"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {});
        assertThat(productDTO).contains(getCheckerDTO);

    }
    @Test
    void testGetProductBetweenPeriodWithBeforeNotFound() throws Exception {
        String contentAsString = mockMvc.perform(get("/api")
                        .param("endYear", "2021"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {});
        assertThat(productDTO).doesNotContain(getCheckerDTO);

    }

    @Test
    void testGetProductBetweenPeriodWithBothFound() throws Exception {
        String contentAsString = mockMvc.perform(get("/api")
                        .param("startYear", "2021")
                        .param("endYear", "2026"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {});
        assertThat(productDTO).contains(getCheckerDTO);

    }
    @Test
    void testGetProductBetweenPeriodWithBothNotFound() throws Exception {
        String contentAsString = mockMvc.perform(get("/api")
                        .param("startYear", "2021")
                        .param("endYear", "2022"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {});
        assertThat(productDTO).doesNotContain(getCheckerDTO);

    }

    @Test
    void testAddProduct() throws Exception {
        String contentAsString = mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postcreaterDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertThat(objectMapper.readValue(contentAsString, ProductDTO.class)).isEqualTo(postcreaterDTO);
        assertThatCode(() -> productRepository.findByName(postcreaterDTO.getName()).orElseThrow(RuntimeException::new)).doesNotThrowAnyException();

    }

    @Test
    void testAddProductsBatch() throws Exception {
        String contentAsString = mockMvc.perform(post("/api/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(postcreaterDTO, postcreaterDTO))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertThat(objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {}
        )).isEqualTo(List.of(postcreaterDTO,postcreaterDTO));

        assertThat(productRepository.findAllByName(postcreaterDTO.getName())).hasSize(2);
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(postcreaterDTO, postcreaterDTO))))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();


        assertFalse(productRepository.findByName(product.getName()).isPresent());
    }

}
