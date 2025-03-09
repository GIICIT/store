package com.example.store.config;

import com.example.store.dao.CustomerDAO;
import com.example.store.dao.OrderDAO;
import com.example.store.dao.ProductDAO;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ManageCache {

    private final org.springframework.cache.CacheManager cacheManager;

    private final ProductDAO productDAO;

    private final CustomerDAO customerDAO;

    private final OrderDAO orderDAO;

    public void updateProducts() {
        Cache productsCache = cacheManager.getCache("store:products");

        if (productsCache != null) {
            List<ProductDTO> allProducts = productDAO.getAllProducts();
            productsCache.put("allProducts", allProducts);
        }
    }

    public void updateCustomers() {
        Cache customersCache = cacheManager.getCache("store:customers");

        if (customersCache != null) {
            List<CustomerDTO> allCustomers = customerDAO.getAllCustomers();
            customersCache.put("allCustomers", allCustomers);
        }
    }

    public void updateOrders() {
        Cache ordersCache = cacheManager.getCache("store:orders");

        if (ordersCache != null) {
            List<OrderDTO> allOrders = orderDAO.getAllOrders();
            ordersCache.put("allOrders", allOrders);
        }
    }
}
