package com.anupam.fullstack.repository;

import com.anupam.fullstack.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
