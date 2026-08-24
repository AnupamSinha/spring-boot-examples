package com.anupam.search.controller;

import com.anupam.search.model.Article;
import com.anupam.search.service.ArticleService;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller exposing search and CRUD endpoints for the Article index.
 * Supports full-text search, fuzzy search, highlighted results, and
 * aggregation queries (articles per author, tag cloud).
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api")
public class SearchController {

    private final ArticleService articleService;

    /**
     * Constructs the SearchController with the required article service.
     *
     * @param articleService the service handling article search and persistence logic
     */
    public SearchController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // --- Full-Text Search ---

    /**
     * Performs a full-text search across article titles and content.
     *
     * @param q the search query string
     * @return 200 OK with matching articles
     */
    @GetMapping("/search")
    public ResponseEntity<List<Article>> search(@RequestParam String q) {
        List<Article> results = articleService.fullTextSearch(q);
        return ResponseEntity.ok(results);
    }

    /**
     * Performs a fuzzy search that tolerates typos in the query.
     *
     * @param q the fuzzy search query string
     * @return 200 OK with matching articles
     */
    @GetMapping("/search/fuzzy")
    public ResponseEntity<List<Article>> fuzzySearch(@RequestParam String q) {
        List<Article> results = articleService.fuzzySearch(q);
        return ResponseEntity.ok(results);
    }

    /**
     * Performs a search with highlighted matching fragments in title and content.
     *
     * @param q the search query string
     * @return 200 OK with search hits including highlight metadata
     */
    @GetMapping("/search/highlight")
    public ResponseEntity<List<SearchHit<Article>>> searchWithHighlight(@RequestParam String q) {
        List<SearchHit<Article>> results = articleService.searchWithHighlighting(q);
        return ResponseEntity.ok(results);
    }

    // --- CRUD ---

    /**
     * Retrieves all articles from the Elasticsearch index.
     *
     * @return 200 OK with the list of all articles
     */
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.findAll());
    }

    /**
     * Creates a single article in the Elasticsearch index.
     *
     * @param article the article to create
     * @return 201 Created with the persisted article (including generated ID)
     */
    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article saved = articleService.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Creates multiple articles in a single bulk operation.
     *
     * @param articles the list of articles to create
     * @return 201 Created with the persisted articles
     */
    @PostMapping("/articles/bulk")
    public ResponseEntity<List<Article>> bulkCreate(@RequestBody List<Article> articles) {
        List<Article> saved = articleService.saveAll(articles);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Deletes an article by its document ID.
     *
     * @param id the Elasticsearch document ID of the article to delete
     * @return 204 No Content on successful deletion
     */
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Aggregations ---

    /**
     * Returns an aggregation of article counts grouped by author.
     *
     * @return 200 OK with a map of author names to article counts
     */
    @GetMapping("/articles/aggregate/authors")
    public ResponseEntity<Map<String, Long>> articlesPerAuthor() {
        return ResponseEntity.ok(articleService.getArticlesPerAuthor());
    }

    /**
     * Returns a tag cloud aggregation showing tag frequency across all articles.
     *
     * @return 200 OK with a map of tag names to occurrence counts
     */
    @GetMapping("/articles/aggregate/tags")
    public ResponseEntity<Map<String, Long>> tagCloud() {
        return ResponseEntity.ok(articleService.getTagCloud());
    }
}
