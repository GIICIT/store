package com.example.store.dao;

import com.example.store.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderDAO {
    List<OrderDTO> getAllOrders();

    Page<OrderDTO> getAllOrders(Pageable pageable);

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO getOrderByID(Long id);
}
