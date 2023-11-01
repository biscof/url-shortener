package com.biscof.urlshortener.service;

import com.biscof.urlshortener.dto.LinkDto;
import com.biscof.urlshortener.dto.LinkResponseDto;
import com.biscof.urlshortener.exception.LinkNotFoundException;
import com.biscof.urlshortener.model.Link;
import com.biscof.urlshortener.repository.LinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class LinkServiceImpl implements LinkService {
    private LinkRepository linkRepository;

    @Override
    public LinkResponseDto createShortLink(LinkDto labelDto) {
        int numFrom = 48; // code for '0': 48-57 => 0-9
        int numTo = 122; // code for 'z' + 65-90 => A-Z, 97-122 => a-z
        int shortUrlLength = 10;
        StringBuilder builder = new StringBuilder();

        new Random().ints(shortUrlLength, numFrom, numTo + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .forEach(builder::appendCodePoint);

        String shortUrl =  builder.toString();
        Link link = new Link(labelDto.getOriginalUrl(), shortUrl);
        linkRepository.save(link);

        return new LinkResponseDto("/l/" + shortUrl);
    }

    @Override
    public String getOriginalUrl(String shortUrl) {
        Link link = linkRepository.findLinkByShortUrl(shortUrl).orElseThrow(
                () -> new LinkNotFoundException(shortUrl)
        );
        return link.getUrl();
    }

}
