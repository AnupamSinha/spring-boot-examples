# HTMX + Spring Boot — Server-Rendered UIs

Modern, interactive UI with **Spring Boot 3.5** and **HTMX 2.0** — no React, no Angular, no build step.

## Features

- HTMX-powered CRUD (create, delete) with partial page updates
- Active search with debounced input (`hx-trigger="input changed delay:500ms"`)
- Thymeleaf fragment rendering for server-side partial responses
- WebJars for HTMX dependency management
- Zero JavaScript written manually

## Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| HTMX | 2.0.0 |
| Thymeleaf | (managed by Spring Boot) |

## Running

```bash
mvn spring-boot:run
```

Open [http://localhost:8080/products](http://localhost:8080/products)

## HTMX Attributes Used

| Attribute | Purpose |
|-----------|---------|
| `hx-get` | Fetch product list fragment |
| `hx-post` | Create new product |
| `hx-delete` | Remove a product |
| `hx-swap` | Control how response replaces DOM |
| `hx-target` | Specify which element to update |
| `hx-trigger` | Define when to fire the request |
| `hx-confirm` | Show confirmation dialog before request |

## Blog Post

See the full walkthrough: [HTMX + Spring Boot — Modern Server-Rendered UIs Without JavaScript Frameworks](https://anupamsinha.github.io/java/spring/2026/08/22/spring-boot-htmx-server-rendered.html)
