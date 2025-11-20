package org.example.productService.config;

import org.example.productService.entity.Product;
import org.example.productService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final ProductRepository productRepository;
    @Bean
    public CommandLineRunner init() {
        return args -> {
            Product product = Product.builder()
                    .name("Product 1")
                    .description("Product 1")
                    .price(30f)
                    .createdAt(LocalDate.of(2021,1,2))
                    .build();
            productRepository.save(product);

            Product product1 = Product.builder()
                    .name("Product 2")
                    .description("Product 2")
                    .price(10f)
                    .createdAt(LocalDate.of(2021,1,1))
                    .build();
            productRepository.save(product1);

            Product product3 = Product.builder()
                    .name("Product 3")
                    .description("Product 3")
                    .price(100f)
                    .createdAt(LocalDate.of(2021,12,31))
                    .build();
            productRepository.save(product3);
        };
    }

}
