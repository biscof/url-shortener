package com.biscof.urlshortener.service;

import com.biscof.urlshortener.dto.LinkDto;
import com.biscof.urlshortener.dto.LinkResponseDto;
import com.biscof.urlshortener.dto.LinkStatsDto;

import java.util.List;

public interface LinkService {

    LinkResponseDto createShortLink(LinkDto labelDto);

    String getOriginalUrl(String shortUrl);

    void incrementRequestCount(String shortUrl);

    LinkStatsDto getLinkStatistics(String shortUrl);

    List<LinkStatsDto> getStatistics(int page, int itemsPerPage);

}
