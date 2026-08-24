package com.anupam.mongo.repository;

import com.anupam.mongo.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data MongoDB repository for {@link Product} documents.
 * <p>
 * Provides standard CRUD operations via {@link MongoRepository} plus
 * custom query methods using MongoDB JSON query syntax.
 * </p>
 *
 * @author Anupam
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    /**
     * Finds all products in the specified category.
     *
     * @param category the product category to filter by
     * @return list of products in the category
     */
    List<Product> findByCategory(String category);

    /**
     * Finds products that contain the specified tag.
     *
     * @param tag the tag to search for
     * @return list of products containing the tag
     */
    List<Product> findByTagsContaining(String tag);

    /**
     * Searches products by name using a case-insensitive regex pattern.
     *
     * @param nameRegex the regex pattern to match against product names
     * @return list of matching products
     */
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Product> searchByName(String nameRegex);

    /**
     * Finds products within the specified price range (inclusive).
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products within the price range
     */
    @Query("{ 'price': { $gte: ?0, $lte: ?1 } }")
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Finds products in a specific category with a price at or below the given maximum.
     *
     * @param category the product category
     * @param maxPrice the maximum allowed price
     * @return list of matching products
     */
    @Query("{ 'category': ?0, 'price': { $lte: ?1 } }")
    List<Product> findByCategoryAndMaxPrice(String category, BigDecimal maxPrice);

    /**
     * Projects only name and price fields for products matching the specified brand.
     *
     * @param brand the brand name to filter by
     * @return list of products with only name and price populated
     */
    @Query(value = "{ 'specification.brand': ?0 }", fields = "{ 'name': 1, 'price': 1 }")
    List<Product> findNameAndPriceByBrand(String brand);
}
