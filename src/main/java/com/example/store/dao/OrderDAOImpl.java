package com.example.store.dao;

import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderDAOImpl implements OrderDAO {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderMapper.ordersToOrderDTOs(orderRepository.findAll());
    }

    @Override
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAll(pageable);
        return ordersPage.map(orderMapper::orderToOrderDTO);
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Product> products = productRepository.findAllById(
                orderDTO.getProducts().stream().map(ProductOrderDTO::getId).toList()
        );

        if (products.size() != orderDTO.getProducts().size()) {
            throw new RuntimeException("One or more products not found");
        }

        Order order = new Order();
        order.setDescription(orderDTO.getDescription());
        order.setCustomer(customer);
        order.setProducts(products);

        return orderMapper.orderToOrderDTO(orderRepository.save(orderMapper.orderDTOToOrder(orderDTO)));
    }

    @Override
    public OrderDTO getOrderByID(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(orderMapper::orderToOrderDTO).orElse(new OrderDTO());
    }
}
