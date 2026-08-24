package com.anupam.performance.repository;

import com.anupam.performance.model.Product;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @QueryHints({
            @QueryHint(name = HibernateHints.HINT_READ_ONLY, value = "true"),
            @QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true")
    })
    List<Product> findByCategory(String category);

    @QueryHints({
            @QueryHint(name = HibernateHints.HINT_READ_ONLY, value = "true"),
            @QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "50")
    })
    List<Product> findAll();

    @QueryHints({
            @QueryHint(name = HibernateHints.HINT_READ_ONLY, value = "true")
    })
    List<Product> findByNameContainingIgnoreCase(String name);
}
