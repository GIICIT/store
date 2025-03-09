package com.example.store.repository;

import com.example.store.dao.ProductDAO;
import com.example.store.dao.ProductDAOImpl;
import com.example.store.dto.ProductDTO;
import com.example.store.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private ProductDAO productDAO;

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
    void setup() {
        productDAO = new ProductDAOImpl(productRepository, ProductMapper.INSTANCE);
    }

    @Test
    void when_find_all_products() {
        List<ProductDTO> products = productDAO.getAllProducts();
        assertThat(products.size()).isEqualTo(500);
    }

    @Test
    void when_find_product_by_id() {
        ProductDTO product = productDAO.getProductById(101L);
        assertThat(product.getId()).isEqualTo(101L);
        assertThat(product.getDescription()).isEqualTo("Ice Cream Maker");
    }

    @Test
    void when_create_product() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription("Test Product");

        ProductDTO createdProduct = productDAO.createProduct(productDTO);
        assertThat(createdProduct.getDescription()).isEqualTo("Test Product");
    }
}