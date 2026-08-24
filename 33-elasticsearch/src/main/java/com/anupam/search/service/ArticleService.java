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

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public ArticleService(ArticleRepository articleRepository,
                          ElasticsearchOperations elasticsearchOperations) {
        this.articleRepository = articleRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public List<Article> saveAll(List<Article> articles) {
        return (List<Article>) articleRepository.saveAll(articles);
    }

    public List<Article> findAll() {
        return (List<Article>) articleRepository.findAll();
    }

    public void deleteById(String id) {
        articleRepository.deleteById(id);
    }

    public List<Article> fullTextSearch(String query) {
        return articleRepository.searchByTitleAndContent(query);
    }

    public List<Article> fuzzySearch(String query) {
        return articleRepository.fuzzySearch(query);
    }

    public List<SearchHit<Article>> searchWithHighlighting(String query) {
        var highlightFields = List.of(
                new HighlightField("title"),
                new HighlightField("content")
        );
        var highlight = new Highlight(highlightFields);

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m
                        .query(query)
                        .fields("title^3", "content")))
                .withHighlightQuery(new HighlightQuery(highlight, Article.class))
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class);
        return searchHits.getSearchHits();
    }

    public Map<String, Long> getArticlesPerAuthor() {
        NativeQuery query = NativeQuery.builder()
                .withAggregation("authors",
                        Aggregation.of(a -> a.terms(t -> t.field("author").size(50))))
                .withMaxResults(0)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
        Map<String, Long> authorCounts = new HashMap<>();

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

    public Map<String, Long> getTagCloud() {
        NativeQuery query = NativeQuery.builder()
                .withAggregation("tags",
                        Aggregation.of(a -> a.terms(t -> t.field("tags").size(100))))
                .withMaxResults(0)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
        Map<String, Long> tagCounts = new HashMap<>();

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
