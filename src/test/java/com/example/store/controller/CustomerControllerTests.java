package com.example.store.controller;

import com.example.store.dao.CustomerDAO;
import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private CustomerDAO customerDAO;

    private Customer customer;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = new CustomerDTO();
        customerDTO.setName("John Doe");
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerDAO.createCustomer(customerDTO)).thenReturn(customerDTO);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        when(customerDAO.getAllCustomers()).thenReturn(List.of(customerDTO));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name").value("John Doe"));
    }

    @Test
    void testFindCustomerByName() throws Exception {
        String name = "John Doe";
        when(customerDAO.findCustomersByName(name)).thenReturn(List.of(customerDTO));

        mockMvc.perform(get("/customer/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

}
