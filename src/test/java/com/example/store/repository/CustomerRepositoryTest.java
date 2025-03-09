package com.example.store.repository;

import com.example.store.dao.CustomerDAO;
import com.example.store.dao.CustomerDAOImpl;
import com.example.store.dto.CustomerDTO;
import com.example.store.mapper.CustomerMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    CustomerDAO customerDAO;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        customerDAO = new CustomerDAOImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    void when_find_all_customers() {
        List<CustomerDTO> customers = customerDAO.getAllCustomers();
        assertThat(customers.size()).isEqualTo(100);
    }

    @Test
    void when_save_customer() {
        CustomerDTO customer = customerDAO.createCustomer(customerBuilder("Test Customer 2"));
        assertThat(customer.getName()).isEqualTo("Test Customer 2");
    }

    @Test
    void when_search_customer_by_name() {
        customerDAO.createCustomer(customerBuilder("Jessy James Wilson"));
        customerDAO.createCustomer(customerBuilder("James Wilson"));
        customerDAO.createCustomer(customerBuilder("John Doe"));
        customerDAO.createCustomer(customerBuilder("Michael James"));
        customerDAO.createCustomer(customerBuilder("Wilson Brown"));
        customerDAO.createCustomer(customerBuilder("Jessy James Wilson"));
        customerDAO.createCustomer(customerBuilder("Michael James"));

        List<CustomerDTO> customers = customerDAO.findCustomersByName("James Wilson");
        assertThat(customers.size()).isEqualTo(6);

        List<CustomerDTO> customers2 = customerDAO.findCustomersByName("Wilson");
        assertThat(customers2.size()).isEqualTo(4);

        List<CustomerDTO> customers3 = customerDAO.findCustomersByName("Michael");
        assertThat(customers3.size()).isEqualTo(2);
    }

    private CustomerDTO customerBuilder(String name) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(name);
        return customerDTO;
    }
}