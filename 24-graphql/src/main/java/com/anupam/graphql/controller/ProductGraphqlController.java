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

@Controller
public class ProductGraphqlController {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ProductGraphqlController(ProductRepository productRepository,
                                    ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    @QueryMapping
    public List<Product> products() {
        return productRepository.findAll();
    }

    @QueryMapping
    public Product productById(@Argument Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument String category) {
        return productRepository.findByCategory(category);
    }

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

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @MutationMapping
    public Review addReview(@Argument Long productId, @Argument ReviewInput input) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Review review = new Review(input.author(), input.comment(), input.rating(), product);
        return reviewRepository.save(review);
    }

    /**
     * BatchMapping prevents the N+1 problem by loading all reviews
     * for a batch of products in a single query.
     */
    @BatchMapping
    public Map<Product, List<Review>> reviews(List<Product> products) {
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        List<Review> allReviews = reviewRepository.findByProductIdIn(productIds);

        Map<Long, List<Review>> reviewsByProductId = allReviews.stream()
                .collect(Collectors.groupingBy(review -> review.getProduct().getId()));

        return products.stream()
                .collect(Collectors.toMap(
                        product -> product,
                        product -> reviewsByProductId.getOrDefault(product.getId(), List.of())
                ));
    }

    record ProductInput(String name, String description, String category, BigDecimal price) {}

    record ReviewInput(String author, String comment, int rating) {}
}
