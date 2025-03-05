package com.example.store.dao;

import com.example.store.dto.CustomerDTO;

import java.util.List;

public interface CustomerDAO {
    List<CustomerDTO> getAllCustomers();

    CustomerDTO createCustomer(CustomerDTO customerDTO);
}
