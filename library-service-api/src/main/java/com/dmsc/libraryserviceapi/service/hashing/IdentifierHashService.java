package com.dmsc.libraryserviceapi.service.hashing;

import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

/**
 * Service interface for handling identifier hashing operations.
 * This interface provides methods to hash identifiers based on the specified
 * book system and to retrieve details from a given hashed string.
 */
public interface IdentifierHashService {

    /**
     * Hashes the provided identifier using the specified book system.
     *
     * @param bookSystemEnum the enum representing the book system to be used for hashing
     * @param id the identifier string to be hashed
     * @return the hashed identifier as a string
     */
    String hash(BookSystemEnum bookSystemEnum, String id);

    /**
     * Retrieves details from a given hashed string.
     *
     * @param hashedString the hashed identifier string from which to retrieve details
     * @return an Optional containing a MultiValueMap with details related to the hashed identifier,
     *         or an empty Optional if no details are found
     */
    Optional<MultiValueMap<String, String>> getDetailsFromHash(String hashedString);
}
