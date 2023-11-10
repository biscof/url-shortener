package com.biscof.urlshortener.service.impl;

import com.biscof.urlshortener.dto.LinkDto;
import com.biscof.urlshortener.dto.LinkResponseDto;
import com.biscof.urlshortener.dto.LinkStatsDto;
import com.biscof.urlshortener.exception.LinkNotFoundException;
import com.biscof.urlshortener.model.Link;
import com.biscof.urlshortener.repository.LinkRepository;
import com.biscof.urlshortener.service.LinkService;
import com.biscof.urlshortener.service.mapper.LinkMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class LinkServiceImpl implements LinkService {

    private LinkRepository linkRepository;

    private LinkMapper mapper;

    @Override
    @Cacheable(cacheNames = "newUrls", key = "#labelDto.originalUrl")
    public LinkResponseDto createShortLink(LinkDto labelDto) {
        String shortUrl;
        String originalUrl = labelDto.getOriginalUrl();
        log.info("Checking if the DB entry already exists");
        Optional<Link> link = linkRepository.findLinkByUrl(originalUrl);

        if (link.isPresent()) {
            shortUrl = link.get().getShortUrl();
        } else {
            shortUrl =  getRandomAlphanumericStr();

            // Ensure the short link is unique.
            while (linkRepository.findLinkByShortUrl(shortUrl).isPresent()) {
                shortUrl =  getRandomAlphanumericStr();
            }

            Link newLink = new Link(originalUrl, shortUrl);
            log.info("Creating new short URL...");
            linkRepository.save(newLink);
        }

        return new LinkResponseDto("/l/" + shortUrl);
    }

    @Override
    @Caching(
            cacheable = {
                    @Cacheable(cacheNames = "originalUrls", key = "#shortUrl")
            },
            evict = {
                    @CacheEvict(cacheNames = "stat", key = "#shortUrl"),
                    @CacheEvict(cacheNames = "stats"),
            }
    )
    public String getOriginalUrl(String shortUrl) {
        log.info("Getting original URL by short URL {}", shortUrl);
        return getLinkByShortUrl(shortUrl).getUrl();
    }

    @Override
    public void incrementRequestCount(String shortUrl) {
        Link link = getLinkByShortUrl(shortUrl);
        link.setRequestCount(link.getRequestCount() + 1);
        linkRepository.save(link);
    }

    @Override
    @Cacheable(cacheNames = "stat", key = "#shortUrl")
    public LinkStatsDto getLinkStatistics(String shortUrl) {
        Link link = getLinkByShortUrl(shortUrl);

        return mapper.toLinkStatsDto(link);
    }

    @Override
    @Cacheable(cacheNames = "stats", key = "#page + '-' + #itemsPerPage")
    public List<LinkStatsDto> getStatistics(int page, int itemsPerPage) {
        final int maxItemsPerPage = 100;

        if (itemsPerPage > maxItemsPerPage) {
            itemsPerPage = maxItemsPerPage;
        }

        Pageable pageSortedByRequestCount = PageRequest.of(page - 1, itemsPerPage,
                Sort.by("requestCount").descending());
        log.info("Getting page {} with {} items...", page, itemsPerPage);
        List<Link> sortedPagedList = linkRepository.findAll(pageSortedByRequestCount).getContent();

        return sortedPagedList.stream()
                .map(mapper::toLinkStatsDto)
                .toList();
    }

    private String getRandomAlphanumericStr() {
        final int numFrom = 48; // code for '0': 48-57 => 0-9
        final int numTo = 122; // code for 'z' + 65-90 => A-Z, 97-122 => a-z
        final int shortUrlLength = 10;
        StringBuilder builder = new StringBuilder();

        new Random().ints(numFrom, numTo + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(shortUrlLength)
                .forEach(builder::appendCodePoint);

        return  builder.toString();
    }

    private Link getLinkByShortUrl(String shortUrl) {
        return linkRepository.findLinkByShortUrl(shortUrl).orElseThrow(
                () -> new LinkNotFoundException("/l/" + shortUrl)
        );
    }

}
