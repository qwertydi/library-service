package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.CreateBookRequest;

import java.util.List;
import java.util.Optional;

/**
 * Interface for managing local book operations, extending common book services.
 */
public interface LocalBookService extends CommonBookServices {
    /**
     * Adds a new book to the local collection.
     *
     * @param request A {@code CreateBookRequest} object containing the details of the book to be added.
     * @return An {@code Optional} containing the {@code BookResponse} if the book is successfully added,
     * or an empty {@code Optional} if the operation fails.
     */
    Optional<BookResponse> addNewBook(CreateBookRequest request);

    /**
     * Retrieves a list of all books available in the local collection.
     *
     * @return A list of {@code BookResponse} objects representing all books in the local collection.
     * If no books are available, an empty list is returned.
     */
    List<BookResponse> searchAllBooks();
}
