package com.dmsc.libraryserviceapi.util;

import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * This utility is responsible to build a unique book ID with one or multiple ids.
 * <p>
 *
 * <ul>
 *     NOTE:
 *     <li>The format of the bookIdentifier: "book?id={originalId}&system={system}".</li>
 * </ul>
 */
public final class IdGeneratorUtil {

    private static final String START_KEY = "book?";
    private static final Character QUERY_PARAM_DELIMITER_KEY_VALUE = '=';
    private static final Character QUERY_PARAM_DELIMITER = '&';

    private IdGeneratorUtil() {
        // prevent initialization
    }

    public static String build(BookSystemEnum bookSystemEnum, String id) {
        return START_KEY +
            "system" +
            QUERY_PARAM_DELIMITER_KEY_VALUE +
            bookSystemEnum +
            QUERY_PARAM_DELIMITER +
            "id" +
            QUERY_PARAM_DELIMITER_KEY_VALUE +
            id;
    }

    public static Optional<MultiValueMap<String, String>> getDetailsFromBookId(String internalBookId) {
        // Spring's UriComponentsBuilder can parse our URI's query parameters if we remove the prefix "book".
        // For example: it can parse "?system=LOCAL&id=123913" (notice the '?' at the beginning).
        return Optional.of(internalBookId)
            .filter(s -> s.startsWith(START_KEY))
            .map(s -> UriComponentsBuilder.fromUriString(s.substring(START_KEY.length() - 1)).build()
                .getQueryParams());
    }
}
