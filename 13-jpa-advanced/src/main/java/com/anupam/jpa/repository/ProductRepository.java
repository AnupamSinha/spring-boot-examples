package com.anupam.jpa.repository;

import com.anupam.jpa.model.Product;
import com.anupam.jpa.model.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product repository demonstrating multiple query approaches.
 *
 * Extends both JpaRepository (standard CRUD) and JpaSpecificationExecutor
 * (dynamic, composable query predicates).
 *
 * @author Anupam
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /** Derived query method - Spring Data generates the SQL from the method name. */
    List<Product> findByCategory(String category);

    /** JPQL query - finds products above a price threshold, ordered descending. */
    @Query("SELECT p FROM Product p WHERE p.price > :threshold ORDER BY p.price DESC")
    List<Product> findHighValueProducts(@Param("threshold") BigDecimal threshold);

    /** Native SQL query - returns category summary with count and average price. */
    @Query(value = "SELECT category, COUNT(*) AS product_count, AVG(price) AS avg_price " +
            "FROM products GROUP BY category ORDER BY avg_price DESC", nativeQuery = true)
    List<Object[]> getCategorySummary();

    /** Interface-based projection - fetches only id, name, and price columns. */
    List<ProductProjection> findAllProjectedBy();
}
