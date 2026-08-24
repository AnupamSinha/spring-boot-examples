package com.anupam.graphql.controller;

import com.anupam.graphql.model.Product;
import com.anupam.graphql.model.Review;
import com.anupam.graphql.repository.ProductRepository;
import com.anupam.graphql.repository.ReviewRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GraphQL controller handling product and review queries and mutations.
 * <p>
 * Uses Spring for GraphQL annotations to wire GraphQL schema operations
 * to Java methods. Includes a {@code @BatchMapping} for reviews to solve
 * the N+1 query problem.
 * </p>
 *
 * @author Anupam
 */
@Controller
public class ProductGraphqlController {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Constructs the controller with required repository dependencies.
     *
     * @param productRepository the JPA repository for product operations
     * @param reviewRepository  the JPA repository for review operations
     */
    public ProductGraphqlController(ProductRepository productRepository,
                                    ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Query resolver: returns all products.
     *
     * @return list of all products
     */
    @QueryMapping
    public List<Product> products() {
        return productRepository.findAll();
    }

    /**
     * Query resolver: finds a product by its unique identifier.
     *
     * @param id the product ID
     * @return the product if found, null otherwise
     */
    @QueryMapping
    public Product productById(@Argument Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Query resolver: finds all products in the given category.
     *
     * @param category the category to filter by
     * @return list of matching products
     */
    @QueryMapping
    public List<Product> productsByCategory(@Argument String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Mutation resolver: creates a new product.
     *
     * @param input the product input data
     * @return the saved product with generated ID
     */
    @MutationMapping
    public Product createProduct(@Argument ProductInput input) {
        Product product = new Product(
                input.name(),
                input.description(),
                input.category(),
                input.price()
        );
        return productRepository.save(product);
    }

    /**
     * Mutation resolver: updates an existing product by ID.
     *
     * @param id    the product ID to update
     * @param input the updated product data
     * @return the updated product
     * @throws RuntimeException if the product is not found
     */
    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument ProductInput input) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(input.name());
                    product.setDescription(input.description());
                    product.setCategory(input.category());
                    product.setPrice(input.price());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    /**
     * Mutation resolver: deletes a product by ID.
     *
     * @param id the product ID to delete
     * @return true if the product existed and was deleted, false otherwise
     */
    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Mutation resolver: adds a review to an existing product.
     *
     * @param productId the product to review
     * @param input     the review content
     * @return the saved review
     * @throws RuntimeException if the product is not found
     */
    @MutationMapping
    public Review addReview(@Argument Long productId, @Argument ReviewInput input) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Review review = new Review(input.author(), input.comment(), input.rating(), product);
        return reviewRepository.save(review);
    }

    /**
     * BatchMapping for reviews — prevents the N+1 problem by loading all reviews
     * for a batch of products in a single query instead of one query per product.
     *
     * @param products the batch of products needing their reviews resolved
     * @return a map associating each product with its list of reviews
     */
    @BatchMapping
    public Map<Product, List<Review>> reviews(List<Product> products) {
        // Collect all product IDs from the batch
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        // Single query to fetch all reviews for the batch
        List<Review> allReviews = reviewRepository.findByProductIdIn(productIds);

        // Group reviews by their owning product ID
        Map<Long, List<Review>> reviewsByProductId = allReviews.stream()
                .collect(Collectors.groupingBy(review -> review.getProduct().getId()));

        // Map each product to its reviews (empty list if none)
        return products.stream()
                .collect(Collectors.toMap(
                        product -> product,
                        product -> reviewsByProductId.getOrDefault(product.getId(), List.of())
                ));
    }

    /** Input record for creating or updating a product via GraphQL mutations. */
    record ProductInput(String name, String description, String category, BigDecimal price) {}

    /** Input record for adding a review via GraphQL mutations. */
    record ReviewInput(String author, String comment, int rating) {}
}
