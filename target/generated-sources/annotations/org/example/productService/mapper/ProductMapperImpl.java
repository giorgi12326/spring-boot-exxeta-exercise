package org.example.productService.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.productService.dto.ProductDTO;
import org.example.productService.entity.Product;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-13T17:04:09+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( productDTO.getName() );
        product.description( productDTO.getDescription() );
        product.createdAt( productDTO.getCreatedAt() );

        return product.build();
    }

    @Override
    public List<Product> toEntities(List<ProductDTO> productDTOs) {
        if ( productDTOs == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( productDTOs.size() );
        for ( ProductDTO productDTO : productDTOs ) {
            list.add( toEntity( productDTO ) );
        }

        return list;
    }

    @Override
    public ProductDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.name( product.getName() );
        productDTO.description( product.getDescription() );
        productDTO.createdAt( product.getCreatedAt() );

        return productDTO.build();
    }

    @Override
    public List<ProductDTO> toDTOs(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<ProductDTO>( products.size() );
        for ( Product product : products ) {
            list.add( toDTO( product ) );
        }

        return list;
    }
}
