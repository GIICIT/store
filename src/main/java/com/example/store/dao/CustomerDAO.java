package com.example.store.dao;

import com.example.store.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerDAO {
    List<CustomerDTO> getAllCustomers();

    Page<CustomerDTO> getAllOrders(Pageable pageable);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> findCustomersByName(String name);
}
