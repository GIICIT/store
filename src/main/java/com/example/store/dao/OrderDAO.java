package com.example.store.dao;

import com.example.store.dto.OrderDTO;

import java.util.List;

public interface OrderDAO {
    List<OrderDTO> getAllOrders();

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO getOrderByID(Long id);
}
