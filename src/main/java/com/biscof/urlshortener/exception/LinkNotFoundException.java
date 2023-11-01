package com.biscof.urlshortener.exception;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException(String shortUrl) {
        super(String.format("URL %s not found.", shortUrl));
    }

}
