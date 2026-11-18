package com.example.catalog.repository;

import com.example.catalog.model.Product;
import java.util.Optional;

public interface ProductRepository {
	
	Optional<Product> findById(Long id);

	Product save(Product product);
}