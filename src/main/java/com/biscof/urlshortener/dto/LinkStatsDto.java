package com.biscof.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkStatsDto implements Serializable {

    private String  shortUrl;
    private String url;
    private int rank;
    private int requestCount;

}
