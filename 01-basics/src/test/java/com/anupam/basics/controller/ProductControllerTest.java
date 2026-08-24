package com.anupam.basics.controller;

import com.anupam.basics.model.Product;
import com.anupam.basics.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link ProductController}.
 *
 * Uses @SpringBootTest with MockMvc to test the full request lifecycle
 * including JSON serialization, validation, and HTTP status codes.
 *
 * @author Anupam
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    /** Verifies that a valid product is created and returns 201 Created. */
    @Test
    void shouldCreateProduct() throws Exception {
        Product product = new Product("Laptop", new BigDecimal("999.99"), "Electronics");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.category").value("Electronics"));
    }

    /** Verifies that the list endpoint returns an array of products. */
    @Test
    void shouldGetAllProducts() throws Exception {
        productService.createProduct(new Product("Phone", new BigDecimal("599.99"), "Electronics"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /** Verifies that a single product can be retrieved by its ID. */
    @Test
    void shouldGetProductById() throws Exception {
        Product saved = productService.createProduct(new Product("Tablet", new BigDecimal("399.99"), "Electronics"));

        mockMvc.perform(get("/api/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tablet"));
    }

    /** Verifies that requesting a non-existent product returns 404. */
    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        mockMvc.perform(get("/api/products/9999"))
                .andExpect(status().isNotFound());
    }

    /** Verifies that an existing product can be updated with new values. */
    @Test
    void shouldUpdateProduct() throws Exception {
        Product saved = productService.createProduct(new Product("Book", new BigDecimal("19.99"), "Education"));
        saved.setName("Updated Book");
        saved.setPrice(new BigDecimal("24.99"));

        mockMvc.perform(put("/api/products/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Book"))
                .andExpect(jsonPath("$.price").value(24.99));
    }

    /** Verifies that deleting a product returns 204 No Content. */
    @Test
    void shouldDeleteProduct() throws Exception {
        Product saved = productService.createProduct(new Product("Pen", new BigDecimal("2.99"), "Stationery"));

        mockMvc.perform(delete("/api/products/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    /** Verifies that submitting an invalid product returns 400 Bad Request. */
    @Test
    void shouldReturnBadRequestForInvalidProduct() throws Exception {
        Product invalid = new Product("", null, "");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    /** Verifies that products can be filtered by category. */
    @Test
    void shouldGetProductsByCategory() throws Exception {
        productService.createProduct(new Product("Mouse", new BigDecimal("29.99"), "Peripherals"));

        mockMvc.perform(get("/api/products/category/Peripherals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Peripherals"));
    }
}
