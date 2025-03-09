package com.example.store.controller;

import com.example.store.config.ManageCache;
import com.example.store.dao.ProductDAO;
import com.example.store.dto.ProductDTO;
import com.example.store.mapper.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ComponentScan(basePackageClasses = ProductMapper.class)
@RequiredArgsConstructor
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductDAO productDAO;

    @MockitoBean
    private ManageCache manageCache;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setDescription("Test Product");
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productDAO.getAllProducts()).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test Product"));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productDAO.getProductById(1L)).thenReturn(productDTO);

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Product"));
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productDAO.createProduct(productDTO)).thenReturn(productDTO);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test Product"));
    }
}