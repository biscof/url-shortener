package com.biscof.urlshortener.factory;

import java.util.stream.Stream;

public class EntityFactory {

    public static Stream<String> getInvalidUrls() {
        final String blankUrl = """
            {
              "originalUrl": "   "
            }
            """;
        final String invalidUrl = """
            {
              "originalUrl": "invalid-url"
            }
            """;

        return Stream.of(blankUrl, invalidUrl);
    }

    public static Stream<String> getEmptyNullUrls() {
        final String emptyUrl = """
            {
              "originalUrl": ""
            }
            """;
        final String nullUrl = """
            {
              "originalUrl": null
            }
            """;

        return Stream.of(emptyUrl, nullUrl);
    }

}
