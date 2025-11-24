package org.example.productService.repository;

import jakarta.persistence.LockModeType;
import org.example.productService.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findByName(String name);
    List<Product> findAllByName(String name);
    List<Product> findByCreatedAtBetween(LocalDate start, LocalDate end);
    List<Product> findByCreatedAtAfter(LocalDate start);
    List<Product> findByCreatedAtBefore(LocalDate end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findProductById(Long id);
}
