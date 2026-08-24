package com.anupam.search.repository;

import com.anupam.search.model.Article;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    List<Article> findByAuthor(String author);

    List<Article> findByTagsContaining(String tag);

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
