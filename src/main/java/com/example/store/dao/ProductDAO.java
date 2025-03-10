package com.example.store.dao;

import com.example.store.dto.ProductDTO;

import java.util.List;

public interface ProductDAO {
    List<ProductDTO> getAllProducts();

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getProductsByIds(List<Long> ids);
}
