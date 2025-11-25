package org.example.productService.mapper;

import org.example.productService.dto.ProductDTO;
import org.example.productService.dto.ReserveResponseDTO;
import org.example.productService.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDTO productDTO);
    List<Product> toEntities(List<ProductDTO> productDTOs);

    ProductDTO toDTO(Product product);
    List<ProductDTO> toDTOs(List<Product> products);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "productDescription", source = "description")
    ReserveResponseDTO toReserveResponse(Product save);

    List<ReserveResponseDTO> toReserveResponses(List<Product> saves);
}
