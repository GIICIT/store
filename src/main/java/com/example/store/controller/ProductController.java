package com.example.store.controller;

import com.example.store.config.ManageCache;
import com.example.store.dao.ProductDAO;
import com.example.store.dto.ProductDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductDAO productDAO;

    private final ManageCache manageCache;

    @Cacheable(value = "store:products", key = "'allProducts'")
    @GetMapping
    @PreAuthorize("hasRole('user')")
    public List<ProductDTO> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    public ProductDTO getAllProductById(@PathVariable Long id) {
        return productDAO.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('admin')")
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO product = productDAO.createProduct(productDTO);
        manageCache.updateProducts();
        return product;
    }
}
