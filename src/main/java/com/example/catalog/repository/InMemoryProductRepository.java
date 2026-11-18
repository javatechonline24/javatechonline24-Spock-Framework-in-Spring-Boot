package com.example.catalog.repository;

import com.example.catalog.model.Product;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepository {
	
	private final Map<Long, Product> products = new ConcurrentHashMap<>();

	public InMemoryProductRepository() {
		products.put(1L, new Product(1L, "Mechanical Keyboard", new BigDecimal("3499.00"), 12));
	}

	@Override
	public Optional<Product> findById(Long id) {
		return Optional.ofNullable(products.get(id));
	}

	@Override
	public Product save(Product product) {
		products.put(product.id(), product);
		return product;
	}
}