package com.example.store.repository;

import com.example.store.dao.CustomerDAO;
import com.example.store.dao.CustomerDAOImpl;
import com.example.store.dto.CustomerDTO;
import com.example.store.mapper.CustomerMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    CustomerDAO customerDAO;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @BeforeAll
    static void setupInfrastructure() {
        System.setProperty("TESTCONTAINERS_DB_HOST", postgres.getHost());
        System.setProperty("TESTCONTAINERS_DB_PORT", postgres.getMappedPort(5432).toString());
        System.setProperty("TESTCONTAINERS_DB_NAME", postgres.getDatabaseName());
        System.setProperty("TESTCONTAINERS_DB_USER", postgres.getUsername());
        System.setProperty("TESTCONTAINERS_DB_PASS", postgres.getPassword());
    }

    @BeforeEach
    void setup(){
        customerDAO = new CustomerDAOImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    void when_find_all_customers(){
        List<CustomerDTO> customers = customerDAO.getAllCustomers();
        assertThat(customers.size()).isEqualTo(100);
    }

    @Test
    void when_save_customer(){
        String sql = "SELECT setval(pg_get_serial_sequence('\"customer\"', 'id'), (SELECT MAX(id) FROM \"customer\"));";
        jdbcTemplate.execute(sql);

        CustomerDTO customer = customerDAO.createCustomer(getCustomer());
        assertThat(customer.getId()).isEqualTo(101L);
        assertThat(customer.getName()).isEqualTo(getCustomer().getName());
    }

    private CustomerDTO getCustomer(){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Test Customer");
        return customerDTO;
    }
}
