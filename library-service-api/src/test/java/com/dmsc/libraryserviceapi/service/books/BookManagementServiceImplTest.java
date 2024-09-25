package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import com.dmsc.libraryserviceapi.service.hashing.IdentifierHashService;
import com.dmsc.libraryserviceapi.util.IdGeneratorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class BookManagementServiceImplTest {

    private BookManagementService classUnderTest;

    private LocalBookService mockLocalBookService;
    private RemoteBookService mockRemoteBookService;
    private IdentifierHashService mockIdentifierHashService;

    @BeforeEach
    void beforeEach() {
        mockLocalBookService = mock(LocalBookService.class);
        mockRemoteBookService = mock(RemoteBookService.class);
        mockIdentifierHashService = mock(IdentifierHashService.class);
        classUnderTest = new BookManagementServiceImpl(mockLocalBookService, mockRemoteBookService, mockIdentifierHashService);
    }

    @Test
    void testGetByTitle() {
        /* Preparation */
        when(mockLocalBookService.searchBookByTitle("title"))
            .thenReturn(Collections.singletonList(BookResponse.builder().title("title").build()));
        when(mockRemoteBookService.searchBookByTitle("title"))
            .thenReturn(Collections.singletonList(BookResponse.builder().title("remoteTitle").build()));

        /* Execution */
        List<BookResponse> response = classUnderTest.searchBookByTitle("title");

        /* Verification */
        assertEquals(2, response.size());

        assertEquals("title", response.get(0).getTitle());
        assertEquals("remoteTitle", response.get(1).getTitle());

        verify(mockLocalBookService).searchBookByTitle("title");
        verify(mockRemoteBookService).searchBookByTitle("title");
    }

    @Test
    void testSearchAllBooks() {
        /* Preparation */
        when(mockLocalBookService.searchAllBooks())
            .thenReturn(Collections.singletonList(BookResponse.builder().title("title").build()));

        /* Execution */
        List<BookResponse> response = classUnderTest.searchAllBooks();

        /* Verification */
        assertEquals(1, response.size());
        verify(mockLocalBookService).searchAllBooks();
        verifyNoInteractions(mockRemoteBookService);

    }

    @Nested
    class SearchById {
        @Test
        void testGetByLocalId() {
            /* Preparation */
            String id = IdGeneratorUtil.build(BookSystemEnum.LOCAL, "1");

            when(mockIdentifierHashService.getDetailsFromHash(id)).thenReturn(IdGeneratorUtil.getDetailsFromBookId(id));
            when(mockLocalBookService.searchBookById("1"))
                .thenReturn(Optional.of(BookResponse.builder().title("title").build()));

            /* Execution */
            Optional<BookResponse> response = classUnderTest.searchBookById(id);

            /* Verification */
            assertTrue(response.isPresent());
            BookResponse bookResponse = response.get();
            assertEquals("title", bookResponse.getTitle());
            verify(mockLocalBookService).searchBookById("1");
        }

        @Test
        void testGetByRemoteId() {
            /* Preparation */
            String id = IdGeneratorUtil.build(BookSystemEnum.OPENLIBRARY, "1");
            when(mockIdentifierHashService.getDetailsFromHash(id)).thenReturn(IdGeneratorUtil.getDetailsFromBookId(id));

            when(mockRemoteBookService.searchBookById("1"))
                .thenReturn(Optional.of(BookResponse.builder().title("title").build()));

            /* Execution */
            Optional<BookResponse> response = classUnderTest.searchBookById(id);

            /* Verification */
            assertTrue(response.isPresent());
            BookResponse bookResponse = response.get();
            assertEquals("title", bookResponse.getTitle());
            verify(mockRemoteBookService).searchBookById("1");
        }
    }
}
