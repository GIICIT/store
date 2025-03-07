package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO productToProductDTO(Product product);

    Product productDTOToProduct(ProductDTO productDTO);

    List<ProductDTO> productsToProductDTOs(List<Product> product);
}
