package com.anupam.mongo.repository;

import com.anupam.mongo.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategory(String category);

    List<Product> findByTagsContaining(String tag);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Product> searchByName(String nameRegex);

    @Query("{ 'price': { $gte: ?0, $lte: ?1 } }")
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("{ 'category': ?0, 'price': { $lte: ?1 } }")
    List<Product> findByCategoryAndMaxPrice(String category, BigDecimal maxPrice);

    @Query(value = "{ 'specification.brand': ?0 }", fields = "{ 'name': 1, 'price': 1 }")
    List<Product> findNameAndPriceByBrand(String brand);
}
