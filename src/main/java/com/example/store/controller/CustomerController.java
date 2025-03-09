package com.example.store.controller;

import com.example.store.config.ManageCache;
import com.example.store.dao.CustomerDAO;
import com.example.store.dto.CustomerDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerDAO customerDAO;

    private final ManageCache manageCache;

    @Cacheable(value = "store:customers", key = "'allCustomers'")
    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO customer = customerDAO.createCustomer(customerDTO);
        manageCache.updateCustomers();
        return customer;
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDTO> findCustomerByName(@PathVariable String name) {
        return customerDAO.findCustomersByName(name);
    }

    @GetMapping("/{page}/{size}")
    public Page<CustomerDTO> getOrderPaging(@PathVariable("page") int page, @PathVariable("size") int size) {
        return customerDAO.getAllCustomersPaging(PageRequest.of(page, size));
    }
}
