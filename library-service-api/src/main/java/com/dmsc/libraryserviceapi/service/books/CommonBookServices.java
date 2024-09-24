package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.model.book.BookResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface that defines common services for handling book-related operations.
 */
public interface CommonBookServices {

    /**
     * Searches for a book by its unique identifier.
     *
     * @param id The unique identifier of the book to search for.
     * @return An {@code Optional} containing the {@code BookResponse} if the book is found,
     * or an empty {@code Optional} if no book with the given ID exists.
     */
    Optional<BookResponse> searchBookById(String id);

    /**
     * Searches for books by their title.
     *
     * @param title The title of the book(s) to search for.
     * @return A list of {@code BookResponse} objects representing books that match the given title.
     * If no books match, an empty list is returned.
     */
    List<BookResponse> searchBookByTitle(String title);
}
