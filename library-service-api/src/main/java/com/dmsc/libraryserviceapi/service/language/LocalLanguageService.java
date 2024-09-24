package com.dmsc.libraryserviceapi.service.language;

import com.dmsc.libraryserviceapi.entity.LanguagesEntity;

/**
 * Interface for managing local language-related operations
 * Will be mostly used to map languages for user created books
 */
public interface LocalLanguageService {
    /**
     * Finds an existing language by its name or creates a new one if it doesn't exist.
     *
     * @param language The name of the language to find or create.
     * @return A {@code LanguagesEntity} representing the found or newly created language.
     */
    LanguagesEntity findOrCreateLanguageByName(String language);
}
