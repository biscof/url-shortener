# URL Shortener

[![build-and-test](https://github.com/biscof/url-shortener/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/biscof/url-shortener/actions/workflows/build-and-test.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/84f00fa961ad66fb21a3/maintainability)](https://codeclimate.com/github/biscof/url-shortener/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/84f00fa961ad66fb21a3/test_coverage)](https://codeclimate.com/github/biscof/url-shortener/test_coverage)


## Overview

This is a simple URL shortener app. The application allows for creating short links for long URLs, tracking link statistics, and redirecting to the original URL using the short link. It is also using Redis for caching.


## Technologies and Dependencies

- Java 17
- Redis 
- H2
- JUnit


## Endpoints

- **Create a short link for a given long URL**

```http
POST /generate
Content-Type: application/json

{
    "originalUrl": "https://test.com/example?param=val"
}
```

Sample response body:

```http
{
    "shortLink": "/l/I3hjhK9x0u"
}
```

- **Redirect to the original URL**

```http
GET /l/{shortUrl}
```

- **Get statistics for a specific short link**

```http
GET /stats/{shortUrl}
```

Sample response body:

```http
{
  "shortUrl": "I3hjhK9x0u",
  "url": "https://test.com/example?param=val",
  "rank": 1,
  "requestCount": 4
}
```

- **Get all statistics**

```http
GET /stats?page=<page number>&count=<items per page>
```

Return paginated statistics for all short links.


## Usage

### Prerequisites

- JDK 17
- Docker
- Docker Compose

### Running

1. Clone this repository:

```bash
git clone https://github.com/biscof/url-shortener.git
```

2. Build app docker image and start it:

```bash
make start-app-in-container
```

Ensure you have a Redis container up and running at `http://localhost:6379`, or you can set your custom host value with environment variable `REDIS_HOST`. Access the app at `http://localhost:8080`.

3. If you want to start the app and Redis server in containers, use this command:

```bash
make start-app-with-redis
```

This will build the app image and start it along with the Redis image using Docker Compose.


## Testing

To run tests, you can use this command:

```bash
make test
```
