package com.dmsc.libraryserviceapi.service.language;

import com.dmsc.libraryserviceapi.entity.LanguagesEntity;
import com.dmsc.libraryserviceapi.repository.LanguageRepository;
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

class LocalLanguageServiceImplTest {

    private LocalLanguageService classUnderTest;

    private LanguageRepository mockLanguageRepository;

    @BeforeEach
    void beforeEach() {
        mockLanguageRepository = mock(LanguageRepository.class);
        classUnderTest = new LocalLanguageServiceImpl(mockLanguageRepository);
    }

    @Test
    void testCreateLanguage() {
        /* Preparation */
        LanguagesEntity languagesEntity = new LanguagesEntity();
        languagesEntity.setLanguage("en");
        languagesEntity.setId(1L);

        when(mockLanguageRepository.findByLanguage("en")).thenReturn(Optional.empty());
        when(mockLanguageRepository.save(any(LanguagesEntity.class)))
            .thenReturn(languagesEntity);

        /* Execution */
        LanguagesEntity response = classUnderTest.findOrCreateLanguageByName("en");

        /* Verification */
        assertNotNull(response);
        verify(mockLanguageRepository).findByLanguage("en");
        ArgumentCaptor<LanguagesEntity> argumentCaptor = ArgumentCaptor.forClass(LanguagesEntity.class);
        verify(mockLanguageRepository).save(argumentCaptor.capture());

        LanguagesEntity value = argumentCaptor.getValue();
        assertEquals("en", value.getLanguage());
    }

    @Test
    void testGetExistingLanguage() {
        /* Preparation */
        LanguagesEntity languagesEntity = new LanguagesEntity();
        languagesEntity.setLanguage("en");
        languagesEntity.setId(1L);

        when(mockLanguageRepository.findByLanguage("en")).thenReturn(Optional.of(languagesEntity));

        /* Execution */
        LanguagesEntity response = classUnderTest.findOrCreateLanguageByName("en");

        /* Verification */
        assertNotNull(response);
        assertEquals("en", response.getLanguage());
        verify(mockLanguageRepository).findByLanguage("en");
        verify(mockLanguageRepository, times(0)).save(any());
    }
}
