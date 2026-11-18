package com.example.catalog.service;

import com.example.catalog.exception.*;
import com.example.catalog.model.Product;
import com.example.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product getProduct(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}

	public Product reserveStock(Long id, int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero");
		}
		Product product = getProduct(id);
		if (product.availableQuantity() < quantity) {
			throw new InsufficientStockException("Insufficient stock for product: " + product.name());
		}
		Product updatedProduct = new Product(product.id(), product.name(), product.price(),
				product.availableQuantity() - quantity);
		return productRepository.save(updatedProduct);
	}
}