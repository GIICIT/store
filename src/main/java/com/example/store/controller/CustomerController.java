package com.example.store.controller;

import com.example.store.dao.CustomerDAO;
import com.example.store.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    @GetMapping("/{page}/{size}")
    public Page<CustomerDTO> getOrderPaging(@PathVariable("page") int page, @PathVariable("size") int size) {
        return customerDAO.getAllOrders(PageRequest.of(page, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerDAO.createCustomer(customerDTO);
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDTO> findCustomerByName(@PathVariable String name) {
        return customerDAO.findCustomersByName(name);
    }
}
