package com.biscof.urlshortener.service;

import com.biscof.urlshortener.dto.LinkDto;
import com.biscof.urlshortener.dto.LinkResponseDto;

public interface LinkService {

    LinkResponseDto createShortLink(LinkDto labelDto);

    String getOriginalUrl(String shortUrl);
}
