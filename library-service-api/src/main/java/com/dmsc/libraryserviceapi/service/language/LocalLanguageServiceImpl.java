package com.dmsc.libraryserviceapi.service.language;

import com.dmsc.libraryserviceapi.entity.LanguagesEntity;
import com.dmsc.libraryserviceapi.repository.LanguageRepository;
import org.springframework.stereotype.Service;

@Service
public class LocalLanguageServiceImpl implements LocalLanguageService {

    private final LanguageRepository languageRepository;

    public LocalLanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public LanguagesEntity findOrCreateLanguageByName(String language) {
        return languageRepository.findByLanguage(language)
            .orElseGet(() -> {
                LanguagesEntity entity = new LanguagesEntity();
                entity.setLanguage(language);
                return languageRepository.save(entity);
            });
    }
}
