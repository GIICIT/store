package com.example.store.repository;

import com.example.store.entity.Customer;

import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {
    public static Specification<Customer> hasNameContaining(String word) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + word.toLowerCase() + "%");
    }
}
