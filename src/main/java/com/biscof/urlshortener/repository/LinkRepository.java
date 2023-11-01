package com.biscof.urlshortener.repository;

import com.biscof.urlshortener.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findLinkByShortUrl(String shortUrl);

}
