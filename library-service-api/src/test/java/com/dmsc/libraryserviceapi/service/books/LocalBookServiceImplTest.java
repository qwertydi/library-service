package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.entity.LocalBookEntity;
import com.dmsc.libraryserviceapi.exception.LibraryInvalidDataException;
import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.CreateBookRequest;
import com.dmsc.libraryserviceapi.repository.LocalBookRepository;
import com.dmsc.libraryserviceapi.service.authors.LocalAuthorsService;
import com.dmsc.libraryserviceapi.service.hashing.IdentifierHashService;
import com.dmsc.libraryserviceapi.service.language.LocalLanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class LocalBookServiceImplTest {

    private LocalBookService classUnderTest;
    private LocalBookRepository mockLocalBookRepository;
    private LocalAuthorsService mockLocalAuthorsService;
    private LocalLanguageService mockLocalLanguageService;

    private static CreateBookRequest getCreateBookRequest() {
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setAuthors(new ArrayList<>());
        createBookRequest.setTitle("title");
        createBookRequest.setPublishYear(String.valueOf(1996));
        createBookRequest.setLanguages(Collections.singletonList("en"));
        return createBookRequest;
    }

    @BeforeEach
    void beforeEach() {
        mockLocalBookRepository = mock(LocalBookRepository.class);
        mockLocalAuthorsService = mock(LocalAuthorsService.class);
        mockLocalLanguageService = mock(LocalLanguageService.class);
        classUnderTest = new LocalBookServiceImpl(
            mockLocalBookRepository,
            mockLocalAuthorsService,
            mockLocalLanguageService
        );
    }

    @Test
    void testSearchAllBooks() {
        /* Preparation */
        when(mockLocalBookRepository.findAll()).thenReturn(new ArrayList<>());

        /* Execution */
        List<BookResponse> bookResponses = classUnderTest.searchAllBooks();

        /* Verification */
        assertNotNull(bookResponses);
        verify(mockLocalBookRepository).findAll();
    }

    @Test
    void testSearchById() {
        /* Preparation */
        when(mockLocalBookRepository.findById(1L)).thenReturn(Optional.empty());

        /* Execution */
        Optional<BookResponse> response = classUnderTest.searchBookById("1");

        /* Verification */
        assertFalse(response.isPresent());
        verify(mockLocalBookRepository).findById(1L);
    }

    @Test
    void testSearchByTitle() {
        /* Preparation */
        when(mockLocalBookRepository.findAllByTitleEqualsIgnoreCase("title")).thenReturn(new ArrayList<>());

        /* Execution */
        List<BookResponse> response = classUnderTest.searchBookByTitle("title");

        /* Verification */
        assertTrue(response.isEmpty());
        verify(mockLocalBookRepository).findAllByTitleEqualsIgnoreCase("title");
    }

    @Nested
    class AddNewBook {
        @Test
        void testAddNew() {
            /* Preparation */
            CreateBookRequest createBookRequest = getCreateBookRequest();

            when(mockLocalBookRepository.findAllByTitleEqualsIgnoreCase("title"))
                .thenReturn(new ArrayList<>());
            when(mockLocalBookRepository.save(any(LocalBookEntity.class)))
                .thenReturn(LocalBookEntity.builder()
                    .id(1L)
                    .publishYear(1996)
                    .authors(new ArrayList<>())
                    .languages(new ArrayList<>())
                    .title("title")
                    .build()
                );

            /* Execution */
            Optional<BookResponse> bookResponse = classUnderTest.addNewBook(createBookRequest);

            /* Validation */
            assertNotNull(bookResponse);

            verify(mockLocalBookRepository).findAllByTitleEqualsIgnoreCase("title");
            ArgumentCaptor<LocalBookEntity> argumentCaptor = ArgumentCaptor.forClass(LocalBookEntity.class);
            verify(mockLocalBookRepository).save(argumentCaptor.capture());

            verifyNoInteractions(mockLocalAuthorsService);
            verify(mockLocalLanguageService).findOrCreateLanguageByName("en");

            LocalBookEntity value = argumentCaptor.getValue();
            assertEquals("title", value.getTitle());
        }

        @Test
        void testExistingBook() {
            /* Preparation */
            CreateBookRequest createBookRequest = getCreateBookRequest();

            LocalBookEntity entity = LocalBookEntity.builder()
                .id(1L)
                .publishYear(1996)
                .authors(new ArrayList<>())
                .languages(new ArrayList<>())
                .title("title")
                .build();

            when(mockLocalBookRepository.findAllByTitleEqualsIgnoreCase("title"))
                .thenReturn(Collections.singletonList(entity));

            /* Execution */
            LibraryInvalidDataException libraryInvalidDataException = assertThrows(LibraryInvalidDataException.class, () -> classUnderTest.addNewBook(createBookRequest));

            /* Validation */
            assertNotNull(libraryInvalidDataException);

            verifyNoInteractions(mockLocalAuthorsService);
            verifyNoInteractions(mockLocalLanguageService);

            verify(mockLocalBookRepository).findAllByTitleEqualsIgnoreCase("title");
        }
    }

}
