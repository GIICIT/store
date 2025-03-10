package com.example.store.dao;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.exception.CustomerNotFoundException;
import com.example.store.exception.ProductNotFoundException;
import com.example.store.mapper.CustomerMapper;
import com.example.store.mapper.OrderMapper;
import com.example.store.mapper.ProductMapper;
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
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;
    private final ProductMapper productMapper;

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
        CustomerDTO customerDTO = customerDAO.findById(orderDTO.getCustomer().getId());

        List<ProductDTO> products = productDAO.getProductsByIds(
                orderDTO.getProducts().stream().map(ProductOrderDTO::getId).toList());

        if (products.size() != orderDTO.getProducts().size()) {
            throw new ProductNotFoundException("One or more products not found");
        }

        Order order = orderMapper.orderDTOToOrder(orderDTO);
        order.setCustomer(customerMapper.customerDTOToCustomer(customerDTO));
        order.setProducts(productMapper.productDTOsToProducts(products));

        return orderMapper.orderToOrderDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO getOrderByID(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(orderMapper::orderToOrderDTO).orElse(new OrderDTO());
    }
}
