package exercise.repository;

import exercise.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCreatedAtBetween(LocalDate start, LocalDate end);
    List<Product> findByCreatedAtAfter(LocalDate start);
    List<Product> findByCreatedAtBefore(LocalDate end);

}
