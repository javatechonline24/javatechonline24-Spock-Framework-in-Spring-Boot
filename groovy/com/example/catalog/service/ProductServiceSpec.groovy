package com.example.catalog.service

import com.example.catalog.model.Product
import com.example.catalog.repository.ProductRepository
import spock.lang.Specification
import java.util.Optional

class ProductServiceSpec extends Specification {
	ProductRepository productRepository = Mock()
	ProductService productService = new ProductService(
	productRepository)

	def "should return product when product exists"() {

		given:
		def product = new Product(
				1L,
				"Mechanical Keyboard",
				new BigDecimal("3499.00"),
				12
		)

		when:
		def result = productService.getProduct(1L)

		then:
		1 * productRepository.findById(1L) >>
				Optional.of(product)

		result.id() == 1L
		result.name() == "Mechanical Keyboard"
		result.availableQuantity() == 12
	}
	
	def "should reduce available stock after reservation"() {
		
			given:
			def product = new Product(
					1L,
					"Mechanical Keyboard",
					new BigDecimal("3499.00"),
					12
			)
		
			when:
			def result = productService.reserveStock(1L, 4)
		
			then:
			1 * productRepository.findById(1L) >>
					Optional.of(product)
		
			1 * productRepository.save({
				it.availableQuantity() == 8
			}) >> { savedProduct -> savedProduct }
		
			result.availableQuantity() == 8
		}
	
		def "should reject invalid reservation quantities"() {
			
				when:
				productService.reserveStock(1L, quantity)
			
				then:
				thrown(IllegalArgumentException)
			
				where:
				quantity << [0, -1, -5]
		}
		
		def "should throw exception when product does not exist"() {
			
				when:
				productService.getProduct(99L)
			
				then:
				1 * productRepository.findById(99L) >>
						Optional.empty()
			
				thrown(ProductNotFoundException)
		}
}
