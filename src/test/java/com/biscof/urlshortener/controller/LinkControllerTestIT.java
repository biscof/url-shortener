package com.biscof.urlshortener.controller;

import com.biscof.urlshortener.model.Link;
import com.biscof.urlshortener.repository.LinkRepository;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet("links.yml")
class LinkControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LinkRepository repository;

    @Test
    void createShortLink() throws Exception {
        final String linkJson = """
            {
              "originalUrl": "https://wikipedia.org"
            }
            """;
        mockMvc.perform(post("/generate")
                    .content(linkJson)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn().getResponse();
        final Optional<Link> newLink = repository.findLinkByUrl("https://wikipedia.org");

        assertTrue(newLink.isPresent());
        assertEquals(10, newLink.get().getShortUrl().length());
    }

    @ParameterizedTest
    @MethodSource("com.biscof.urlshortener.factory.EntityFactory#getInvalidUrls")
    void createShortLinkInvalidUrl(String inputUrlJson) throws Exception {
        final String invalidUrlExceptionMsg = "Invalid URL provided.";

        mockMvc.perform(post("/generate")
                        .content(inputUrlJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code").value(400))
                .andExpect(jsonPath("$[0].status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$[0].message").value(invalidUrlExceptionMsg));
    }

    @ParameterizedTest
    @MethodSource("com.biscof.urlshortener.factory.EntityFactory#getEmptyNullUrls")
    void createShortLinkEmptyOrNullUrl(String inputUrlJson) throws Exception {
        final String invalidUrlExceptionMsg = "URL can't be empty.";

        mockMvc.perform(post("/generate")
                        .content(inputUrlJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code").value(400))
                .andExpect(jsonPath("$[0].status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$[0].message").value(invalidUrlExceptionMsg));
    }

    @Test
    void redirectToOriginalUrl() throws Exception {
        final String shortUrl = "h5Uik0NmQL";
        final Link link = repository.findLinkByShortUrl(shortUrl).get();

        mockMvc.perform(get("/l/{shortUrl}", shortUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(link.getUrl()));

        assertEquals(4, link.getRequestCount());
    }

    @Test
    void redirectToOriginalUrlInvalidShortUrl() throws Exception {
        final String shortUrl = "invalid-url";
        final String invalidUrlExceptionMsg = String.format("URL /l/%s not found.", shortUrl);

        mockMvc.perform(get("/l/{shortUrl}", shortUrl))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(invalidUrlExceptionMsg));
    }

    @Test
    void getLinkStatistics() throws Exception {
        final String shortUrl = "jV46cTyjlP";

        mockMvc.perform(get("/stats/{shortUrl}", shortUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.rank").value(1))
                .andExpect(jsonPath("$.requestCount").value(5));
    }

    @Test
    void getAllStatisticsAllOnOnePage() throws Exception {
        mockMvc.perform(get("/stats?page=1&count=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[0].requestCount").value(5))
                .andExpect(jsonPath("$[1].requestCount").value(3))
                .andExpect(jsonPath("$[1].url").value("https://example.com"))
                .andExpect(jsonPath("$[2].rank").value(3))
                .andExpect(jsonPath("$[2].requestCount").value(2));
    }

    @Test
    void getAllStatisticsAllSecondPage() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/stats?page=2&count=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].rank").value(3))
                .andExpect(jsonPath("$.[0].requestCount").value(2))
                .andExpect(jsonPath("$.[0].url").value("https://test.io"))
                .andReturn().getResponse();

        assertFalse(response.getContentAsString().contains("https://test.com"));
        assertFalse(response.getContentAsString().contains("jV46cTyjlP"));
    }
}
