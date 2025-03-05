package com.example.store.dao;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderDAOImpl implements OrderDAO {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderMapper.ordersToOrderDTOs(orderRepository.findAll());
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        return orderMapper.orderToOrderDTO(orderRepository.save(orderMapper.orderDTOToOrder(orderDTO)));
    }

    @Override
    public OrderDTO getOrderByID(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(orderMapper::orderToOrderDTO).orElse(new OrderDTO());
    }
}
