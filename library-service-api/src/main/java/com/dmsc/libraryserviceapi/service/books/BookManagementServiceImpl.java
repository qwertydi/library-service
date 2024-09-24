package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.exception.LibraryInvalidDataException;
import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import com.dmsc.libraryserviceapi.model.book.CreateBookRequest;
import com.dmsc.libraryserviceapi.util.IdGeneratorUtil;
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

    public BookManagementServiceImpl(LocalBookService localBookService, RemoteBookService remoteBookService) {
        this.localBookService = localBookService;
        this.remoteBookService = remoteBookService;
    }

    @Override
    public BookResponse createBook(CreateBookRequest request) {
        return localBookService.addNewBook(request)
            .orElse(null);
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
        MultiValueMap<String, String> detailsFromBookId = IdGeneratorUtil.getDetailsFromBookId(id)
            .orElseThrow(() -> new IllegalAccessError(""));

        BookSystemEnum bookSystemEnum = BookSystemEnum.valueOf(detailsFromBookId.getFirst("system"));
        String mappedId = detailsFromBookId.getFirst("id");

        switch (bookSystemEnum) {
            case LOCAL -> {
                return localBookService.searchBookById(mappedId);
            }
            case OPENLIBRARY -> {
                return remoteBookService.searchBookById(mappedId);
            }
            default -> throw new LibraryInvalidDataException("Third party not implemented", HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @Override
    public List<BookResponse> searchBookByTitle(String title) {
        List<BookResponse> localResponses = localBookService.searchBookByTitle(title);
        List<BookResponse> remoteResponses = remoteBookService.searchBookByTitle(title);

        ArrayList<BookResponse> bookResponses = new ArrayList<>();
        bookResponses.addAll(localResponses);
        bookResponses.addAll(remoteResponses);

        return bookResponses;
    }
}
