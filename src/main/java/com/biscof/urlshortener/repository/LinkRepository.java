package com.biscof.urlshortener.repository;

import com.biscof.urlshortener.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findLinkByShortUrl(String shortUrl);

    Optional<Link> findLinkByUrl(String url);

    @Query("SELECT COUNT(link) + 1 FROM Link link WHERE link.requestCount > :requestCount")
    int findRankByRequestCount(@Param("requestCount") int requestCount);

}
