package org.example.productService.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.productService.dto.ProductDTO;
import org.example.productService.dto.ReserveResponseDTO;
import org.example.productService.entity.Product;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T09:11:20+0400",
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

        product.id( productDTO.getId() );
        product.name( productDTO.getName() );
        product.description( productDTO.getDescription() );
        product.createdAt( productDTO.getCreatedAt() );
        product.quantity( productDTO.getQuantity() );
        product.price( productDTO.getPrice() );
        product.userId( productDTO.getUserId() );

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

        productDTO.id( product.getId() );
        productDTO.name( product.getName() );
        productDTO.description( product.getDescription() );
        productDTO.price( product.getPrice() );
        productDTO.quantity( product.getQuantity() );
        productDTO.userId( product.getUserId() );
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

    @Override
    public ReserveResponseDTO toReserveResponse(Product save) {
        if ( save == null ) {
            return null;
        }

        ReserveResponseDTO reserveResponseDTO = new ReserveResponseDTO();

        reserveResponseDTO.setId( save.getId() );
        reserveResponseDTO.setQuantity( save.getQuantity() );

        return reserveResponseDTO;
    }

    @Override
    public List<ReserveResponseDTO> toReserveResponses(List<Product> saves) {
        if ( saves == null ) {
            return null;
        }

        List<ReserveResponseDTO> list = new ArrayList<ReserveResponseDTO>( saves.size() );
        for ( Product product : saves ) {
            list.add( toReserveResponse( product ) );
        }

        return list;
    }
}
