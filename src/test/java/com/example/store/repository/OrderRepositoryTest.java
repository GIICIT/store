package com.example.store.repository;

import com.example.store.dao.CustomerDAO;
import com.example.store.dao.CustomerDAOImpl;
import com.example.store.dao.OrderDAO;
import com.example.store.dao.OrderDAOImpl;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.mapper.CustomerMapper;
import com.example.store.mapper.OrderMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    CustomerDAO customerDAO;

    OrderDAO orderDAO;

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
        orderDAO = new OrderDAOImpl(orderRepository, OrderMapper.INSTANCE);
        customerDAO = new CustomerDAOImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    void when_find_all_orders(){
        List<OrderDTO> orders = orderDAO.getAllOrders();
        assertThat(orders.size()).isEqualTo(10000);
    }

    @Test
    void when_find_order_by_id(){
        OrderDTO order = orderDAO.getOrderByID(1L);
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getDescription()).isEqualTo("Handcrafted Soft Chair");
        assertThat(order.getCustomer().getId()).isEqualTo(6L);
        assertThat(order.getCustomer().getName()).isEqualTo("Vicki Kutch");
    }

    @Test
    void when_save_order(){
        CustomerDTO customer = customerDAO.createCustomer(getCustomer());
        assertThat(customer.getName()).isEqualTo(getCustomer().getName());

        OrderDTO oderDTO = orderDAO.createOrder(getOrder());
        assertThat(oderDTO.getDescription()).isEqualTo(getOrder().getDescription());
    }

    private OrderDTO getOrder(){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setDescription("Test Order");
        orderDTO.setCustomer(getOrderCustomerDTO());
        return orderDTO;
    }

    private OrderCustomerDTO getOrderCustomerDTO(){
        OrderCustomerDTO orderCustomerDTO = new OrderCustomerDTO();
        orderCustomerDTO.setId(101L);
        orderCustomerDTO.setName("Test Customer");
        return orderCustomerDTO;
    }

    private CustomerDTO getCustomer(){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Test Customer");
        return customerDTO;
    }
}
