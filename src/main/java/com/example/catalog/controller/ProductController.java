package com.example.catalog.controller;

import com.example.catalog.model.*;
import com.example.catalog.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/{id}")
	public Product getProduct(@PathVariable Long id) {
		return productService.getProduct(id);
	}

	@PostMapping("/{id}/reserve")
	public Product reserveStock(@PathVariable Long id, @RequestBody ReserveRequest request) {
		return productService.reserveStock(id, request.quantity());
	}
}
