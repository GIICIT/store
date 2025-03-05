package com.example.store.controller;

import com.example.store.dao.OrderDAO;
import com.example.store.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderDAO orderDAO;

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        return orderDAO.createOrder(orderDTO);
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderByID(@PathVariable Long id) {
        return orderDAO.getOrderByID(id);
    }
}
