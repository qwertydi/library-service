package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.openlibraryapi.model.BookSdk;
import com.dmsc.openlibraryapi.model.SearchSdkRequest;
import com.dmsc.openlibraryapi.model.SearchSdkResponse;
import com.dmsc.openlibraryapi.model.SearchTitleSdkRequest;
import com.dmsc.openlibraryapi.sdk.SearchApiSdk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RemoteBookServiceImplTest {

    private RemoteBookService classUnderTest;
    private SearchApiSdk mockSearchApiSdk;

    @BeforeEach
    void beforeEach() {
        mockSearchApiSdk = mock(SearchApiSdk.class);
        classUnderTest = new RemoteBookServiceImpl(mockSearchApiSdk);
    }

    @Test
    void testGetById() {
        assertFalse(classUnderTest.searchBookById("id").isPresent());
    }

    @Nested
    class SearchByTitle {
        @Test
        void testResults() {
            /* Preparation */
            SearchSdkRequest request = SearchTitleSdkRequest.builder()
                .title("title")
                .build();

            BookSdk bookSdk = new BookSdk();
            bookSdk.setTitle("title");
            bookSdk.setLanguage(Collections.singletonList("en"));
            bookSdk.setAuthorName(Collections.singletonList("J. K. Rowling"));
            bookSdk.setFirstPublishYear(1996);

            SearchSdkResponse sdkResponse = new SearchSdkResponse();
            sdkResponse.setDocs(Collections.singletonList(bookSdk));

            when(mockSearchApiSdk.searchBooks(request)).thenReturn(sdkResponse);

            /* Execution */
            List<BookResponse> responses = classUnderTest.searchBookByTitle("title");

            /* Verification */
            assertNotNull(responses);
            assertEquals(1, responses.size());
            BookResponse bookResponse = responses.get(0);
            assertEquals("title", bookResponse.getTitle());
            assertEquals(List.of("J. K. Rowling"), bookResponse.getAuthors());
            assertEquals(List.of("en"), bookResponse.getLanguages());
            assertEquals(1996, bookResponse.getPublishYear());
            verify(mockSearchApiSdk).searchBooks(request);
        }

        @Test
        void testNoResults() {
            /* Preparation */
            SearchSdkRequest request = SearchTitleSdkRequest.builder()
                .title("title")
                .build();

            SearchSdkResponse sdkResponse = new SearchSdkResponse();
            sdkResponse.setDocs(new ArrayList<>());

            when(mockSearchApiSdk.searchBooks(request)).thenReturn(sdkResponse);

            /* Execution */
            List<BookResponse> responses = classUnderTest.searchBookByTitle("title");

            /* Verification */
            assertNotNull(responses);
            assertEquals(0, responses.size());
            verify(mockSearchApiSdk).searchBooks(request);
        }
    }
}
