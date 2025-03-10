package com.example.store.mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    OrderCustomerDTO customerToOrderCustomerDTO(Customer customer);

    ProductOrderDTO orderToOrderProductDTO(Product Product);

    @Mapping(target = "customer", source = "customer", qualifiedByName = "mapCustomer")
    @Mapping(target = "products", source = "products", qualifiedByName = "mapProducts")
    Order orderDTOToOrder(OrderDTO orderDTO);

    @Named("mapCustomer")
    static Customer mapCustomer(OrderCustomerDTO dto) {
        return dto != null ? new Customer(dto.getId(), dto.getName()) : null;
    }

    @Named("mapProducts")
    static List<Product> mapProducts(List<ProductOrderDTO> dtos) {
        return dtos != null
                ? dtos.stream()
                        .map(dto -> new Product(dto.getId(), dto.getDescription()))
                        .collect(Collectors.toList())
                : null;
    }
}
