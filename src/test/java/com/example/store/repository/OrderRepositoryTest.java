package com.example.store.repository;

import com.example.store.dao.*;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.mapper.CustomerMapper;
import com.example.store.mapper.OrderMapper;
import com.example.store.mapper.ProductMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
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

    @Autowired
    private ProductRepository productRepository;

    CustomerDAO customerDAO;

    OrderDAO orderDAO;

    ProductDAO productDAO;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @BeforeAll
    static void setupInfrastructure() {
        System.setProperty("TESTCONTAINERS_DB_HOST", postgres.getHost());
        System.setProperty(
                "TESTCONTAINERS_DB_PORT", postgres.getMappedPort(5432).toString());
        System.setProperty("TESTCONTAINERS_DB_NAME", postgres.getDatabaseName());
        System.setProperty("TESTCONTAINERS_DB_USER", postgres.getUsername());
        System.setProperty("TESTCONTAINERS_DB_PASS", postgres.getPassword());
    }

    @BeforeEach
    void setup() {
        orderDAO = new OrderDAOImpl(orderRepository, customerRepository, productRepository, OrderMapper.INSTANCE);
        customerDAO = new CustomerDAOImpl(customerRepository, CustomerMapper.INSTANCE);
        productDAO = new ProductDAOImpl(productRepository, ProductMapper.INSTANCE);
    }

    @Test
    void when_find_all_orders() {
        List<OrderDTO> orders = orderDAO.getAllOrders();
        assertThat(orders.size()).isEqualTo(10000);
    }

    @Test
    void when_find_order_by_id() {
        OrderDTO order = orderDAO.getOrderByID(1L);
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getDescription()).isEqualTo("Handcrafted Soft Chair");
        assertThat(order.getCustomer().getId()).isEqualTo(6L);
        assertThat(order.getCustomer().getName()).isEqualTo("Vicki Kutch");
    }

    @Test
    void when_create_order() {
        OrderDTO orderDTO = orderDAO.createOrder(orderBuilder());
        assertThat(orderDTO.getId()).isEqualTo(10001L);
        assertThat(orderDTO.getDescription()).isEqualTo("Test Order");

        assertThat(orderDTO.getCustomer().getId()).isEqualTo(23);
        assertThat(orderDTO.getCustomer().getName()).isEqualTo("Julio Stark");

        assertThat(orderDTO.getProducts().size()).isEqualTo(4);

        assertThat(orderDTO.getProducts().get(0).getId()).isEqualTo(12);
        assertThat(orderDTO.getProducts().get(0).getDescription()).isEqualTo("Noise Cancelling Headphones");
        assertThat(orderDTO.getProducts().get(1).getId()).isEqualTo(13);
        assertThat(orderDTO.getProducts().get(1).getDescription()).isEqualTo("Gaming Mouse Pad");
        assertThat(orderDTO.getProducts().get(2).getId()).isEqualTo(23);
        assertThat(orderDTO.getProducts().get(2).getDescription()).isEqualTo("Digital Camera");
        assertThat(orderDTO.getProducts().get(3).getId()).isEqualTo(16);
        assertThat(orderDTO.getProducts().get(3).getDescription()).isEqualTo("Fitness Tracker");
    }

    private OrderDTO orderBuilder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setDescription("Test Order");
        orderDTO.setCustomer(CustomerMapper.INSTANCE.customerDTOToOrderCustomerDTO(getCustomer()));
        orderDTO.setProducts(ProductMapper.INSTANCE.productToProductOrderDTO(getProduct()));
        return orderDTO;
    }

    private CustomerDTO getCustomer() {
        return customerDAO.findById(23L);
    }

    private List<ProductDTO> getProduct() {
        List<ProductDTO> products = new ArrayList<>();
        products.add(productDAO.getProductById(12L));
        products.add(productDAO.getProductById(13L));
        products.add(productDAO.getProductById(23L));
        products.add(productDAO.getProductById(16L));
        return products;
    }
}
