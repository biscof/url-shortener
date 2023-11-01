package com.biscof.urlshortener.controller;

import com.biscof.urlshortener.dto.LinkDto;
import com.biscof.urlshortener.service.LinkServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LinkController {

    @Autowired
    private LinkServiceImpl linkService;

    @PostMapping(path = "/generate")
    public ResponseEntity<Object> createShortLink(
            @Validated @RequestBody LinkDto labelDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(linkService.createShortLink(labelDto));
    }

    @GetMapping(path = "/l/{shortUrl}")
    public void redirectToOriginalUrl(
            HttpServletResponse response,
            @PathVariable String shortUrl
    ) {
        String originalUrl = linkService.getOriginalUrl(shortUrl);

        try {
            response.sendRedirect(originalUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
