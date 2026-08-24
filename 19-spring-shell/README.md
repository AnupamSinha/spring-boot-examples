# Spring Shell CLI

An interactive command-line application built with Spring Shell, demonstrating file operations, HTTP commands, and custom greetings.

## Features

- **File Commands** — list files, get file info, count words
- **HTTP Commands** — simple HTTP GET requests, ping
- **Greeting Commands** — hello with options, date, version
- Tab completion and interactive mode

## Prerequisites

- Java 21
- Maven

## Running

```bash
# Build the application
./mvnw clean package

# Run in interactive mode
java -jar target/spring-shell-cli-0.0.1-SNAPSHOT.jar

# Or run directly
./mvnw spring-boot:run
```

## Commands

```
shell:> help
shell:> hello --name Anupam
shell:> list-files /tmp
shell:> file-info pom.xml
shell:> word-count README.md
shell:> http-get https://httpbin.org/get
shell:> ping google.com
shell:> date
shell:> version
```

## Blog Post

Detailed walkthrough: [Building a CLI with Spring Shell](https://anupamsinha.github.io/posts/spring-shell-cli-application/)
