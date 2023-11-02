package com.biscof.urlshortener.service.mapper;

import com.biscof.urlshortener.dto.LinkStatsDto;
import com.biscof.urlshortener.model.Link;
import com.biscof.urlshortener.repository.LinkRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LinkMapper {

    @Autowired
    public LinkRepository repository;

    @Mapping(target = "rank", expression = "java(calculateRank(link))")
    public abstract LinkStatsDto toLinkStatsDto(Link link);

    int calculateRank(Link link) {
        return repository.findRankByRequestCount(link.getRequestCount());
    }

}
