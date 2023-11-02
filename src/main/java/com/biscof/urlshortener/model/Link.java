package com.biscof.urlshortener.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;


@Entity
@Table(name = "links")
@Getter
@NoArgsConstructor
public class Link {

    public Link(String url, String shortUrl) {
        this.url = url;
        this.shortUrl = shortUrl;
        this.requestCount = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String shortUrl;

    @Setter
    private int requestCount;

    @CreationTimestamp
    private Instant createdAt;

}
