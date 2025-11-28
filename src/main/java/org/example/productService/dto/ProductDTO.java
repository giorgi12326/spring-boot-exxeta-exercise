package org.example.productService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    Long id;

    @NotNull
    String name;

    String description;

    Float price;

    Integer quantity;

    Long userId;

    LocalDate createdAt;

}
