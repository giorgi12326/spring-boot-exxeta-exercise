package exercise.repository;

import exercise.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    private Product product1;
    private Product product2;

    @DynamicPropertySource
    static void registerManualDb(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> "jdbc:postgresql://localhost:5432/mypg");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "yourpassword");
    }

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();

        product1 = Product.builder()
                .name("product1")
                .description("description1")
                .createdAt(LocalDate.now())
                .build();

        product2 = Product.builder()
                .name("product2")
                .description("description2")
                .createdAt(LocalDate.now().plusYears(1))
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    void testFindAll(){
        List<Product> all = productRepository.findAll();
        assertTrue(all.containsAll(List.of(product1,product2)));
        assertEquals(2,all.size());
    }

    @Test
    void testFindAllByName(){
        Product product3 = Product.builder()
                .name("product1")
                .description("description2")
                .build();
        productRepository.save(product3);

        List<Product> all = productRepository.findAllByName(product1.getName());
        assertTrue(all.containsAll(List.of(product1,product3)));
        assertEquals(2,all.size());

    }

    @Test
    void findByCreatedAtBetween(){
        List<Product> byCreatedAtBetween = productRepository.findByCreatedAtBetween(LocalDate.of(2025, 1, 1), LocalDate.of(2027, 1, 1));
        assertEquals(2, byCreatedAtBetween.size());
        assertTrue(byCreatedAtBetween.containsAll(List.of(product1,product2)));
    }

    @Test
    void findByCreatedAtBetween2(){
        List<Product> byCreatedAtBetween = productRepository.findByCreatedAtBetween(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1));
        assertEquals(1, byCreatedAtBetween.size());
        assertTrue(byCreatedAtBetween.contains(product1));
    }

    @Test
    void findByCreatedAtBetween3(){
        List<Product> byCreatedAtBetween = productRepository.findByCreatedAtBetween(LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1));
        assertEquals(1, byCreatedAtBetween.size());
        assertTrue(byCreatedAtBetween.contains(product2));
    }

    @Test
    void findByCreatedAtBetween4(){
        List<Product> byCreatedAtBetween = productRepository.findByCreatedAtBetween(LocalDate.of(2027, 1, 1), LocalDate.of(2028, 1, 1));
        assertEquals(0, byCreatedAtBetween.size());
    }

}