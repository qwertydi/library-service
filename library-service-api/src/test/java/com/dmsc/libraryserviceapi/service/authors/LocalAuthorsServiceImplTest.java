package com.dmsc.libraryserviceapi.service.authors;

import com.dmsc.libraryserviceapi.entity.AuthorEntity;
import com.dmsc.libraryserviceapi.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocalAuthorsServiceImplTest {

    private LocalAuthorsService classUnderTest;

    private AuthorRepository mockAuthorRepository;

    @BeforeEach
    void beforeEach() {
        mockAuthorRepository = mock(AuthorRepository.class);
        classUnderTest = new LocalAuthorsServiceImpl(mockAuthorRepository);
    }

    @Test
    void testAddNewAuthor() {
        /* Preparation */
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName("name");
        authorEntity.setId(1L);

        when(mockAuthorRepository.findByName("name")).thenReturn(Optional.empty());
        when(mockAuthorRepository.save(any(AuthorEntity.class)))
            .thenReturn(authorEntity);

        /* Execution */
        AuthorEntity response = classUnderTest.findOrCreateAuthorByName("name");

        /* Verification */
        assertNotNull(response);
        verify(mockAuthorRepository).findByName("name");
        ArgumentCaptor<AuthorEntity> argumentCaptor = ArgumentCaptor.forClass(AuthorEntity.class);
        verify(mockAuthorRepository).save(argumentCaptor.capture());

        AuthorEntity value = argumentCaptor.getValue();
        assertEquals("name", value.getName());
    }

    @Test
    void testGetExistingAuthor() {
        /* Preparation */
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName("name");
        authorEntity.setId(1L);

        when(mockAuthorRepository.findByName("name")).thenReturn(Optional.of(authorEntity));

        /* Execution */
        AuthorEntity response = classUnderTest.findOrCreateAuthorByName("name");

        /* Verification */
        assertNotNull(response);
        assertEquals("name", response.getName());
        verify(mockAuthorRepository).findByName("name");
        verify(mockAuthorRepository, times(0)).save(any());
    }
}
