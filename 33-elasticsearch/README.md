# 33 — Spring Boot + Elasticsearch Full-Text Search

Demonstrates full-text search with Spring Boot and Elasticsearch including document mapping, custom queries, fuzzy matching, highlighting, and aggregations.

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring Data Elasticsearch
- Elasticsearch 8.14

## Running

### 1. Start Elasticsearch

```bash
docker-compose up -d
```

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

### 3. Test the API

```bash
# Create an article
curl -X POST http://localhost:8080/api/articles \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Getting Started with Elasticsearch",
    "content": "Elasticsearch is a distributed search and analytics engine...",
    "author": "anupam",
    "tags": ["elasticsearch", "search", "java"],
    "publishedAt": "2026-08-22"
  }'

# Full-text search
curl "http://localhost:8080/api/search?q=elasticsearch"

# Fuzzy search (handles typos)
curl "http://localhost:8080/api/search/fuzzy?q=elastcsearch"

# Search with highlighting
curl "http://localhost:8080/api/search/highlight?q=distributed"

# Aggregation: articles per author
curl http://localhost:8080/api/articles/aggregate/authors

# Aggregation: tag cloud
curl http://localhost:8080/api/articles/aggregate/tags
```

## API Endpoints

| Method | Endpoint                          | Description                |
|--------|-----------------------------------|----------------------------|
| GET    | /api/search?q=keyword             | Full-text search           |
| GET    | /api/search/fuzzy?q=keyword       | Fuzzy search (typo-tolerant) |
| GET    | /api/search/highlight?q=keyword   | Search with highlighting   |
| GET    | /api/articles                     | List all articles          |
| POST   | /api/articles                     | Create an article          |
| POST   | /api/articles/bulk                | Bulk create articles       |
| DELETE | /api/articles/{id}                | Delete an article          |
| GET    | /api/articles/aggregate/authors   | Articles per author        |
| GET    | /api/articles/aggregate/tags      | Tag cloud                  |
