package org.example.productService.mapper;

import org.example.productService.dto.ProductDTO;
import org.example.productService.entity.Product;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")
//    @Mapping(source = "createdAt", target = "createdAt")
    Product toEntity(ProductDTO productDTO);

    List<Product> toEntities(List<ProductDTO> productDTOs);

//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")
//    @Mapping(source = "createdAt", target = "createdAt")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOs(List<Product> products);
}
