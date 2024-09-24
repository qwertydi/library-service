package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.CreateBookRequest;

import java.util.List;

/**
 * Interface for managing book-related operations, extending common book services.
 */
public interface BookManagementService extends CommonBookServices {

    /**
     * Creates a new book with the provided details.
     *
     * @param request A {@code CreateBookRequest} object containing the information needed to create a new book.
     * @return A {@code BookResponse} object representing the newly created book.
     */
    BookResponse createBook(CreateBookRequest request);

    /**
     * Retrieves a list of all available books.
     *
     * @return A list of {@code BookResponse} objects representing all books in the system.
     * If no books are available, an empty list is returned.
     */
    List<BookResponse> searchAllBooks();
}
