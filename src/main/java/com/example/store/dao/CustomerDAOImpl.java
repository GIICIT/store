package com.example.store.dao;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.CustomerSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomerDAOImpl implements CustomerDAO {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll());
    }

    @Override
    public Page<CustomerDTO> getAllOrders(Pageable pageable) {
        Page<Customer> ordersPage = customerRepository.findAll(pageable);
        return ordersPage.map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        return customerMapper.customerToCustomerDTO(customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO)));
    }

    @Override
    public List<CustomerDTO> findCustomersByName(String name) {
        String[] words = name.split("\\s+");

        Set<String> returnedNames = new HashSet<>();
        List<Customer> finalResults = new ArrayList<>();

        for (String word : words) {
            List<Customer> customers = customerRepository.findAll(CustomerSpecifications.hasNameContaining(word));

            for (Customer customer : customers) {
                String uniqueKey = customer.getId() + "-" + customer.getName();

                if (!returnedNames.contains(uniqueKey)) {
                    finalResults.add(customer);
                    returnedNames.add(uniqueKey);
                }
            }
        }

        return customerMapper.customersToCustomerDTOs(finalResults);
    }
}
