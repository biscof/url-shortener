package com.biscof.urlshortener.dto;

import com.biscof.urlshortener.dto.constraint.LinkUrl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {

    @LinkUrl
    private String originalUrl;

}
