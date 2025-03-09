package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderCustomerDTO;
import com.example.store.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "orders", ignore = true) // Adjust mapping as needed
    CustomerDTO customerToCustomerDTO(Customer customer);

    @Mapping(target = "orders", ignore = true) // Adjust mapping as needed
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customers);

    OrderCustomerDTO customerDTOToOrderCustomerDTO(CustomerDTO customer);
}
