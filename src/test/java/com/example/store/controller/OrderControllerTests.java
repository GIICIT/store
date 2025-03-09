package com.example.store.controller;

import com.example.store.dao.CustomerDAO;
import com.example.store.dao.OrderDAO;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.mapper.CustomerMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
@RequiredArgsConstructor
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderDAO orderDAO;

    @MockitoBean
    private CustomerDAO customerDAO;

    private OrderDTO orderDTO;
    private OrderCustomerDTO orderCustomerDTO;
    private CustomerDTO customerDTO;


    @BeforeEach
    void setUp() {
        orderCustomerDTO = new OrderCustomerDTO();
        orderCustomerDTO.setId(1L);
        orderCustomerDTO.setName("John Doe");

        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Test Order");
        orderDTO.setCustomer(orderCustomerDTO);
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(orderDAO.getAllOrders()).thenReturn(List.of(orderDTO));

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test Order"))
                .andExpect(jsonPath("$[0].customer.name").value("John Doe"));
    }

    @Test
    void testCreateOrder() throws Exception {
        when(customerDAO.createCustomer(customerDTO)).thenReturn(customerDTO);
        when(orderDAO.createOrder(orderDTO)).thenReturn(orderDTO);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test Order"))
                .andExpect(jsonPath("$.customer.name").value("John Doe"));
    }

    @Test
    void testGetOrderByID() throws Exception {
        when(orderDAO.getOrderByID(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Order"))
                .andExpect(jsonPath("$.customer.name").value("John Doe")); // Ensure customer is included
    }

    @Test
    void testGetOrderPaging() throws Exception {
        Page<OrderDTO> orderPage = new PageImpl<>(List.of(orderDTO));
        when(orderDAO.getAllOrders(PageRequest.of(0, 10))).thenReturn(orderPage);

        mockMvc.perform(get("/order/0/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value("Test Order"));
    }
}
