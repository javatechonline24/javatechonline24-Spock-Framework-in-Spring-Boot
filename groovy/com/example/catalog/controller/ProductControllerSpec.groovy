package com.example.catalog.controller

import com.example.catalog.model.Product
import com.example.catalog.service.ProductService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc

import spock.lang.Specification

import java.math.BigDecimal

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerSpec extends Specification {

	@Autowired
	MockMvc mockMvc

	@SpringBean
	ProductService productService

	def "should return product details through REST API"() {

		given:
		def product = new Product(
				1L,
				"Mechanical Keyboard",
				new BigDecimal("3499.00"),
				12
		)

		productService.getProduct(1L) >> product

		expect:
		mockMvc.perform(get("/api/products/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name")
						.value("Mechanical Keyboard"))
				.andExpect(jsonPath("$.availableQuantity")
						.value(12))
	}

	def "should reserve product stock through REST API"() {

		given:
		def updatedProduct = new Product(
				1L,
				"Mechanical Keyboard",
				new BigDecimal("3499.00"),
				8
		)

		productService.reserveStock(1L, 4) >>
				updatedProduct

		when:
		def response = mockMvc.perform(
				post("/api/products/1/reserve")
						.contentType(MediaType.APPLICATION_JSON)
						.content('{"quantity":4}')
		)

		then:
		response.andExpect(status().isOk())
				.andExpect(jsonPath("$.availableQuantity")
						.value(8))
	}
	
	def "should return 404 when product is missing"() {
		
			given:
			productService.getProduct(99L) >>
					{ throw new ProductNotFoundException(99L) }
		
			expect:
			mockMvc.perform(get("/api/products/99"))
					.andExpect(status().isNotFound())
	}
}