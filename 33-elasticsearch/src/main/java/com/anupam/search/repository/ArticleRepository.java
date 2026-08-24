package com.anupam.search.repository;

import com.anupam.search.model.Article;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Elasticsearch repository for {@link Article} documents.
 * Provides both derived query methods and custom JSON-based Elasticsearch queries
 * for full-text search, fuzzy matching, and filtered searches.
 *
 * @author Anupam
 */
@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    /**
     * Finds articles by exact author name match (keyword field).
     *
     * @param author the author name to filter by
     * @return list of articles by the specified author
     */
    List<Article> findByAuthor(String author);

    /**
     * Finds articles that contain a specific tag.
     *
     * @param tag the tag to search for
     * @return list of articles containing the specified tag
     */
    List<Article> findByTagsContaining(String tag);

    /**
     * Performs a multi-match full-text search across title (boosted 3x) and content fields.
     * Uses "best_fields" strategy to score based on the highest-scoring field.
     *
     * @param query the search query string
     * @return list of articles matching the search query
     */
    @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": ["title^3", "content"],
                "type": "best_fields"
              }
            }
            """)
    List<Article> searchByTitleAndContent(String query);

    /**
     * Performs a fuzzy multi-match search across title (boosted 3x) and content fields.
     * Fuzziness AUTO allows for typo tolerance based on term length.
     *
     * @param query the search query string (typos are tolerated)
     * @return list of articles matching the fuzzy search query
     */
    @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": ["title^3", "content"],
                "fuzziness": "AUTO"
              }
            }
            """)
    List<Article> fuzzySearch(String query);

    /**
     * Performs a full-text search filtered by a specific author.
     * Combines a multi-match query in the "must" clause with an author filter.
     *
     * @param query  the search query string
     * @param author the author name to filter results by
     * @return list of articles matching both the query and author filter
     */
    @Query("""
            {
              "bool": {
                "must": [
                  { "multi_match": { "query": "?0", "fields": ["title^3", "content"] } }
                ],
                "filter": [
                  { "term": { "author": "?1" } }
                ]
              }
            }
            """)
    List<Article> searchByQueryAndAuthor(String query, String author);
}
