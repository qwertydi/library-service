package com.dmsc.libraryserviceapi.service.authors;

import com.dmsc.libraryserviceapi.entity.AuthorEntity;

/**
 * Interface for managing local author-related.
 */
public interface LocalAuthorsService {

    /**
     * Finds an existing author by their name or creates a new author if none exists.
     *
     * @param name The name of the author to find or create.
     * @return An {@code AuthorEntity} representing the found or newly created author.
     */
    AuthorEntity findOrCreateAuthorByName(String name);
}
