package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.exception.LibraryInvalidDataException;
import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import com.dmsc.libraryserviceapi.model.book.CreateBookRequest;
import com.dmsc.libraryserviceapi.service.hashing.IdentifierHashService;
import com.dmsc.libraryserviceapi.service.notification.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookManagementServiceImpl implements BookManagementService {

    private final LocalBookService localBookService;
    private final RemoteBookService remoteBookService;
    private final IdentifierHashService identifierHashService;
    private final HashBookId hashBookId;
    private final NotificationService notificationService;

    public BookManagementServiceImpl(LocalBookService localBookService,
                                     RemoteBookService remoteBookService,
                                     IdentifierHashService identifierHashService,
                                     NotificationService notificationService) {
        this.localBookService = localBookService;
        this.remoteBookService = remoteBookService;
        this.identifierHashService = identifierHashService;
        this.hashBookId = (book, systemEnum) -> {
            String hashedId = identifierHashService.hash(BookSystemEnum.LOCAL, book.getId());
            book.setId(hashedId);
            return book;
        };
        this.notificationService = notificationService;
    }

    @Override
    public BookResponse createBook(CreateBookRequest request) {
        BookResponse bookResponse = localBookService.addNewBook(request)
            .orElseThrow(() -> new LibraryInvalidDataException("Unable to create book", HttpStatus.BAD_REQUEST));

        notificationService.sendEmailNotification(bookResponse);

        return bookResponse;
    }

    /**
     * Search API doesn't return books if a title or search parameter is sent.
     * For this reason books returned come from {@link LocalBookService#searchAllBooks()}
     *
     * @return List<BookResponse>
     */
    @Override
    public List<BookResponse> searchAllBooks() {
        return localBookService.searchAllBooks();
    }

    @Override
    public Optional<BookResponse> searchBookById(String id) {
        MultiValueMap<String, String> detailsFromBookId = identifierHashService.getDetailsFromHash(id)
            .orElseThrow(() -> new IllegalAccessError(""));

        BookSystemEnum bookSystemEnum = BookSystemEnum.valueOf(detailsFromBookId.getFirst("system"));
        String mappedId = detailsFromBookId.getFirst("id");

        switch (bookSystemEnum) {
            case LOCAL -> {
                return localBookService.searchBookById(mappedId).map(x -> {
                    x.setId(id);
                    return x;
                });
            }
            case OPENLIBRARY -> {
                return remoteBookService.searchBookById(mappedId).map(x -> {
                    x.setId(id);
                    return x;
                });
            }
            default -> throw new LibraryInvalidDataException("Third party not implemented", HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @Override
    public List<BookResponse> searchBookByTitle(String title) {
        List<BookResponse> localResponses = localBookService.searchBookByTitle(title)
            .stream()
            .map(book -> hashBookId.apply(book, BookSystemEnum.LOCAL))
            .toList();
        List<BookResponse> remoteResponses = remoteBookService.searchBookByTitle(title)
            .stream()
            .map(book -> hashBookId.apply(book, BookSystemEnum.OPENLIBRARY))
            .toList();

        ArrayList<BookResponse> bookResponses = new ArrayList<>();
        bookResponses.addAll(localResponses);
        bookResponses.addAll(remoteResponses);

        return bookResponses;
    }

    /**
     * A functional interface for hashing a book's ID.
     * <p>
     * This interface defines a method to apply a hashing function to a
     * {@link BookResponse} object, given a specific {@link BookSystemEnum}.
     * The result of the operation should be a modified or new instance of
     * {@link BookResponse} with the hashed ID.
     * </p>
     */
    @FunctionalInterface
    public interface HashBookId {
        /**
         * Applies a hashing operation to the given book and system enum.
         *
         * @param book       the {@link BookResponse} object containing the book details
         * @param systemEnum the {@link BookSystemEnum} that indicates the
         *                   specific system context for hashing
         * @return a {@link BookResponse} object with the hashed ID
         */
        BookResponse apply(BookResponse book, BookSystemEnum systemEnum);
    }
}
