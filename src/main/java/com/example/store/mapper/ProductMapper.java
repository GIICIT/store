package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "orderIds", source = "orders", qualifiedByName = "mapOrderIds")
    ProductDTO productToProductDTO(Product product);

    @Mapping(target = "orders", ignore = true)
    Product productDTOToProduct(ProductDTO productDTO);

    List<ProductDTO> productsToProductDTOs(List<Product> products);

    List<ProductOrderDTO> productToProductOrderDTO(List<ProductDTO> product);

    @Named("mapOrderIds")
    static List<Long> mapOrderIds(List<Order> orders) {
        return orders != null ? orders.stream().map(Order::getId).collect(Collectors.toList()) : null;
    }
}
