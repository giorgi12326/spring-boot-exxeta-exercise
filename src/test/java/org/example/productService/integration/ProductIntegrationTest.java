package org.example.productService.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.productService.dto.FullUserDTO;
import org.example.productService.dto.ProductDTO;
import org.example.productService.entity.Product;
import org.example.productService.entity.Role;
import org.example.productService.feign.UserClient;
import org.example.productService.repository.ProductRepository;
import org.example.productService.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductIntegrationTest {

    private ProductDTO postcreaterDTO;

    private Product product;

    @MockitoBean
    private UserClient userClient;

    private String adminToken;
    private String sellerToken;
    private String customerToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO getCheckerDTO;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private FullUserDTO admin;
    private FullUserDTO seller;
    private FullUserDTO customer;

//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
//            .withDatabaseName("testdb")
//            .withUsername("postgres")
//            .withPassword("yourpassword");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ()-> "jdbc:postgresql://localhost:5432/postgres");
        registry.add("spring.datasource.username", ()-> "postgres");
        registry.add("spring.datasource.password", ()-> "1970");
    }

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil();
        adminToken = jwtUtil.generateToken("admin");
        sellerToken = jwtUtil.generateToken("seller");
        customerToken = jwtUtil.generateToken("customer");

        admin = FullUserDTO.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role(Role.ADMIN).build();

        seller = FullUserDTO.builder()
                .username("seller")
                .password(passwordEncoder.encode("seller"))
                .role(Role.SELLER).build();

        customer = FullUserDTO.builder()
                .username("customer")
                .password(passwordEncoder.encode("customer"))
                .role(Role.CUSTOMER).build();

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
    void testGetProductBetweenPeriodADMIN() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +adminToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).contains(getCheckerDTO);
    }

    @Test
    void testGetProductBetweenPeriodCUSTOMER() throws Exception {
        when(userClient.getUserByUsername("customer")).thenReturn(customer);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +customerToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).contains(getCheckerDTO);
    }

    @Test
    void testGetProductBetweenPeriodSELLER() throws Exception {
        when(userClient.getUserByUsername("seller")).thenReturn(seller);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +sellerToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).contains(getCheckerDTO);
    }

    @Test
    void testGetProductBetweenPeriodWithAfterFound() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +adminToken)
                        .param("startYear", "2021"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).contains(getCheckerDTO);

    }

    @Test
    void testGetProductBetweenPeriodWithAfterNotFound() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCheckerDTO))
                .param("startYear", "2026"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).doesNotContain(getCheckerDTO);
    }

    @Test
    void testGetProductBetweenPeriodWithBeforeFound() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +adminToken)
                        .param("endYear", "2026"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).contains(getCheckerDTO);

    }
    @Test
    void testGetProductBetweenPeriodWithBeforeNotFound() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +adminToken)
                        .param("endYear", "2021"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).doesNotContain(getCheckerDTO);

    }

    @Test
    void testGetProductBetweenPeriodWithBothFound() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +adminToken)
                        .param("startYear", "2021")
                        .param("endYear", "2026"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).contains(getCheckerDTO);

    }
    @Test
    void testGetProductBetweenPeriodWithBothNotFound() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(get("/api")
                        .header("Authorization", "Bearer " +adminToken)
                        .param("startYear", "2021")
                        .param("endYear", "2022"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProductDTO> productDTO = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(productDTO).doesNotContain(getCheckerDTO);

    }

    @Test
    void testAddProductADMIN() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(post("/api")
                        .header("Authorization", "Bearer " +adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postcreaterDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertThat(objectMapper.readValue(contentAsString, ProductDTO.class)).isEqualTo(postcreaterDTO);
        assertThatCode(() -> productRepository.findByName(postcreaterDTO.getName()).orElseThrow(RuntimeException::new)).doesNotThrowAnyException();

    }

    @Test
    void testAddProductSELLER() throws Exception {
        when(userClient.getUserByUsername("seller")).thenReturn(seller);
        String contentAsString = mockMvc.perform(post("/api")
                        .header("Authorization", "Bearer " +sellerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postcreaterDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertThat(objectMapper.readValue(contentAsString, ProductDTO.class)).isEqualTo(postcreaterDTO);
        assertThatCode(() -> productRepository.findByName(postcreaterDTO.getName()).orElseThrow(RuntimeException::new)).doesNotThrowAnyException();

    }

    @Test
    void testAddProductCUSTOMER() throws Exception {
        when(userClient.getUserByUsername("customer")).thenReturn(customer);
        mockMvc.perform(post("/api")
                        .header("Authorization", "Bearer " +customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postcreaterDTO)))
                .andExpect(status().isForbidden());

    }

    @Test
    void testAddProductsBatchADMIN() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        String contentAsString = mockMvc.perform(post("/api/batch")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(postcreaterDTO, postcreaterDTO))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertThat(objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {}
        )).isEqualTo(List.of(postcreaterDTO,postcreaterDTO));

        assertThat(productRepository.findAllByName(postcreaterDTO.getName())).hasSize(2);
    }

    @Test
    void testAddProductsBatchSELLER() throws Exception {
        when(userClient.getUserByUsername("seller")).thenReturn(seller);
        String contentAsString = mockMvc.perform(post("/api/batch")
                        .header("Authorization", "Bearer " + sellerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(postcreaterDTO, postcreaterDTO))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertThat(objectMapper.readValue(contentAsString, new TypeReference<List<ProductDTO>>() {}
        )).isEqualTo(List.of(postcreaterDTO,postcreaterDTO));

        assertThat(productRepository.findAllByName(postcreaterDTO.getName())).hasSize(2);
    }

    @Test
    void testAddProductsBatchCUSTOMER() throws Exception {
        when(userClient.getUserByUsername("customer")).thenReturn(customer);
        mockMvc.perform(post("/api/batch")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(postcreaterDTO, postcreaterDTO))))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteProductADMIN() throws Exception {
        when(userClient.getUserByUsername("admin")).thenReturn(admin);
        mockMvc.perform(delete("/api/" + product.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(postcreaterDTO, postcreaterDTO))))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();


        assertFalse(productRepository.findByName(product.getName()).isPresent());
    }
    @Test
    void testDeleteProductSELLER() throws Exception {
        when(userClient.getUserByUsername("seller")).thenReturn(seller);
        mockMvc.perform(delete("/api/" + product.getId())
                        .header("Authorization", "Bearer " + sellerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(postcreaterDTO, postcreaterDTO))))
                .andExpect(status().isForbidden());

    }
    @Test
    void testDeleteProductCUSTOMER() throws Exception {
        when(userClient.getUserByUsername("customer")).thenReturn(customer);
        mockMvc.perform(delete("/api/" + product.getId())
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(postcreaterDTO, postcreaterDTO))))
                .andExpect(status().isForbidden());
    }

}
