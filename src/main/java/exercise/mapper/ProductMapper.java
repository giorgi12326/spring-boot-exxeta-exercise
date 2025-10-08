package exercise.mapper;

import exercise.dto.ProductDTO;
import exercise.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductDTO productDTO);

    List<Product> toEntities(List<ProductDTO> productDTOs);

    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOs(List<Product> products);
}
