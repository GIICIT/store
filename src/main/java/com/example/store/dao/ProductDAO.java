package com.example.store.dao;

import com.example.store.dto.ProductDTO;

import java.util.List;

public interface ProductDAO {
    List<ProductDTO> getAllProducts();

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getAllProductById(Long id);
}
