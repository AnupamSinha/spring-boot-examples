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

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

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

    @Test
    void shouldGetAllProducts() throws Exception {
        productService.createProduct(new Product("Phone", new BigDecimal("599.99"), "Electronics"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldGetProductById() throws Exception {
        Product saved = productService.createProduct(new Product("Tablet", new BigDecimal("399.99"), "Electronics"));

        mockMvc.perform(get("/api/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tablet"));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        mockMvc.perform(get("/api/products/9999"))
                .andExpect(status().isNotFound());
    }

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

    @Test
    void shouldDeleteProduct() throws Exception {
        Product saved = productService.createProduct(new Product("Pen", new BigDecimal("2.99"), "Stationery"));

        mockMvc.perform(delete("/api/products/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestForInvalidProduct() throws Exception {
        Product invalid = new Product("", null, "");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetProductsByCategory() throws Exception {
        productService.createProduct(new Product("Mouse", new BigDecimal("29.99"), "Peripherals"));

        mockMvc.perform(get("/api/products/category/Peripherals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Peripherals"));
    }
}
