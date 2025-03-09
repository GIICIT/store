package com.example.store.controller;

import com.example.store.config.ManageCache;
import com.example.store.dao.OrderDAO;
import com.example.store.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderDAO orderDAO;

    private final ManageCache manageCache;

    @Cacheable(value = "store:orders", key = "'allOrders'")
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO order = orderDAO.createOrder(orderDTO);
        manageCache.updateOrders();
        return order;
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderByID(@PathVariable Long id) {
        return orderDAO.getOrderByID(id);
    }

    @GetMapping("/{page}/{size}")
    public Page<OrderDTO> getOrderPaging(@PathVariable("page") int page, @PathVariable("size") int size) {
        return orderDAO.getAllOrders(PageRequest.of(page, size));
    }
}
