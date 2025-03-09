package com.example.store.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CachePopulationRunner implements CommandLineRunner {

    private final ManageCache manageCache;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        manageCache.updateProducts();
        manageCache.updateCustomers();
        manageCache.updateOrders();
    }
}
