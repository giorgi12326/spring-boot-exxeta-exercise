package exercise.mapper;

import exercise.dto.ProductDTO;
import exercise.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "createdAt", target = "createdAt")
    Product toEntity(ProductDTO productDTO);

    List<Product> toEntities(List<ProductDTO> productDTOs);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "createdAt", target = "createdAt")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOs(List<Product> products);
}
