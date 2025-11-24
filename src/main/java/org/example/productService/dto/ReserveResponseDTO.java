package org.example.productService.dto;

import lombok.Data;

@Data
public class ReserveResponseDTO {
    Long productId;

    Integer quantity;

    Float price;
}
