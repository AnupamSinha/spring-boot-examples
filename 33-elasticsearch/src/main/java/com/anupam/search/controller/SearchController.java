package com.anupam.search.controller;

import com.anupam.search.model.Article;
import com.anupam.search.service.ArticleService;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final ArticleService articleService;

    public SearchController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // --- Full-Text Search ---

    @GetMapping("/search")
    public ResponseEntity<List<Article>> search(@RequestParam String q) {
        List<Article> results = articleService.fullTextSearch(q);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/fuzzy")
    public ResponseEntity<List<Article>> fuzzySearch(@RequestParam String q) {
        List<Article> results = articleService.fuzzySearch(q);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/highlight")
    public ResponseEntity<List<SearchHit<Article>>> searchWithHighlight(@RequestParam String q) {
        List<SearchHit<Article>> results = articleService.searchWithHighlighting(q);
        return ResponseEntity.ok(results);
    }

    // --- CRUD ---

    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.findAll());
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article saved = articleService.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/articles/bulk")
    public ResponseEntity<List<Article>> bulkCreate(@RequestBody List<Article> articles) {
        List<Article> saved = articleService.saveAll(articles);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Aggregations ---

    @GetMapping("/articles/aggregate/authors")
    public ResponseEntity<Map<String, Long>> articlesPerAuthor() {
        return ResponseEntity.ok(articleService.getArticlesPerAuthor());
    }

    @GetMapping("/articles/aggregate/tags")
    public ResponseEntity<Map<String, Long>> tagCloud() {
        return ResponseEntity.ok(articleService.getTagCloud());
    }
}
