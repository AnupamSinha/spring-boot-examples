package com.anupam.search.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import com.anupam.search.model.Article;
import com.anupam.search.repository.ArticleRepository;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer encapsulating Elasticsearch operations for articles.
 * Provides full-text search, fuzzy search, search with highlighting,
 * and aggregation capabilities using both the repository abstraction
 * and the lower-level ElasticsearchOperations API.
 *
 * @author Anupam
 */
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * Constructs the ArticleService with required dependencies.
     *
     * @param articleRepository        the Spring Data Elasticsearch repository
     * @param elasticsearchOperations  the lower-level Elasticsearch operations template
     */
    public ArticleService(ArticleRepository articleRepository,
                          ElasticsearchOperations elasticsearchOperations) {
        this.articleRepository = articleRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * Saves a single article to the Elasticsearch index.
     *
     * @param article the article to persist
     * @return the saved article with generated ID
     */
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    /**
     * Saves multiple articles to the Elasticsearch index in bulk.
     *
     * @param articles the list of articles to persist
     * @return the saved articles with generated IDs
     */
    public List<Article> saveAll(List<Article> articles) {
        return (List<Article>) articleRepository.saveAll(articles);
    }

    /**
     * Retrieves all articles from the Elasticsearch index.
     *
     * @return the complete list of articles
     */
    public List<Article> findAll() {
        return (List<Article>) articleRepository.findAll();
    }

    /**
     * Deletes an article by its Elasticsearch document ID.
     *
     * @param id the document ID to delete
     */
    public void deleteById(String id) {
        articleRepository.deleteById(id);
    }

    /**
     * Performs a full-text search across title and content fields.
     * Title matches are boosted 3x relative to content matches.
     *
     * @param query the search query string
     * @return list of matching articles
     */
    public List<Article> fullTextSearch(String query) {
        return articleRepository.searchByTitleAndContent(query);
    }

    /**
     * Performs a fuzzy search that tolerates typos using Elasticsearch's AUTO fuzziness.
     *
     * @param query the search query string (typos tolerated)
     * @return list of matching articles
     */
    public List<Article> fuzzySearch(String query) {
        return articleRepository.fuzzySearch(query);
    }

    /**
     * Performs a search with highlighted matching fragments in the title and content fields.
     * Uses NativeQuery with a multi-match query and highlight configuration.
     *
     * @param query the search query string
     * @return list of search hits with highlight metadata
     */
    public List<SearchHit<Article>> searchWithHighlighting(String query) {
        // Configure highlight fields for title and content
        var highlightFields = List.of(
                new HighlightField("title"),
                new HighlightField("content")
        );
        var highlight = new Highlight(highlightFields);

        // Build native query with multi-match and highlighting
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m
                        .query(query)
                        .fields("title^3", "content")))
                .withHighlightQuery(new HighlightQuery(highlight, Article.class))
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class);
        return searchHits.getSearchHits();
    }

    /**
     * Performs a terms aggregation to count articles per author.
     * Returns up to 50 author buckets sorted by document count.
     *
     * @return map of author names to their article counts
     */
    public Map<String, Long> getArticlesPerAuthor() {
        // Build aggregation query with zero results (only aggregation data needed)
        NativeQuery query = NativeQuery.builder()
                .withAggregation("authors",
                        Aggregation.of(a -> a.terms(t -> t.field("author").size(50))))
                .withMaxResults(0)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
        Map<String, Long> authorCounts = new HashMap<>();

        // Extract bucket results from the terms aggregation
        if (searchHits.hasAggregations()) {
            ElasticsearchAggregations aggregations =
                    (ElasticsearchAggregations) searchHits.getAggregations();
            var authorAgg = aggregations.aggregations().get(0).aggregation().getAggregate();
            List<StringTermsBucket> buckets = authorAgg.sterms().buckets().array();
            for (StringTermsBucket bucket : buckets) {
                authorCounts.put(bucket.key().stringValue(), bucket.docCount());
            }
        }

        return authorCounts;
    }

    /**
     * Performs a terms aggregation to build a tag cloud with occurrence counts.
     * Returns up to 100 tag buckets sorted by document count.
     *
     * @return map of tag names to their occurrence counts
     */
    public Map<String, Long> getTagCloud() {
        // Build aggregation query with zero results (only aggregation data needed)
        NativeQuery query = NativeQuery.builder()
                .withAggregation("tags",
                        Aggregation.of(a -> a.terms(t -> t.field("tags").size(100))))
                .withMaxResults(0)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
        Map<String, Long> tagCounts = new HashMap<>();

        // Extract bucket results from the terms aggregation
        if (searchHits.hasAggregations()) {
            ElasticsearchAggregations aggregations =
                    (ElasticsearchAggregations) searchHits.getAggregations();
            var tagAgg = aggregations.aggregations().get(0).aggregation().getAggregate();
            List<StringTermsBucket> buckets = tagAgg.sterms().buckets().array();
            for (StringTermsBucket bucket : buckets) {
                tagCounts.put(bucket.key().stringValue(), bucket.docCount());
            }
        }

        return tagCounts;
    }
}
