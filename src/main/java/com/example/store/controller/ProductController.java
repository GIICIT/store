package com.example.store.controller;

import com.example.store.dao.ProductDAO;
import com.example.store.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductDAO productDAO;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getAllProductById(@PathVariable Long id) {
        return productDAO.getAllProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createOrder(@RequestBody ProductDTO productDTO) {
        return productDAO.createProduct(productDTO);
    }
}
