package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import com.dmsc.libraryserviceapi.util.IdGeneratorUtil;
import com.dmsc.openlibraryapi.model.SearchSdkRequest;
import com.dmsc.openlibraryapi.model.SearchSdkResponse;
import com.dmsc.openlibraryapi.sdk.SearchApiSdk;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RemoteBookServiceImpl implements RemoteBookService {

    private final SearchApiSdk searchApiSdk;

    public RemoteBookServiceImpl(SearchApiSdk searchApiSdk) {
        this.searchApiSdk = searchApiSdk;
    }

    @Override
    public Optional<BookResponse> searchBookById(String id) {
        // TODO: Require implement/change existing SDK
        return Optional.empty();
    }

    @Override
    public List<BookResponse> searchBookByTitle(String title) {
        SearchSdkRequest request = SearchSdkRequest.builder()
            .title(title)
            .build();

        SearchSdkResponse searchSdkResponse = searchApiSdk.searchBooks(request);
        return searchSdkResponse.getDocs()
            .stream()
            .map(bookSdk -> {
                BookResponse bookResponse = new BookResponse();
                bookResponse.setTitle(bookSdk.getTitle());
                bookResponse.setAuthors(bookSdk.getAuthorKey());
                bookResponse.setLanguages(bookSdk.getLanguage());
                bookResponse.setId(IdGeneratorUtil.build(BookSystemEnum.OPENLIBRARY, bookSdk.getKey()));
                return bookResponse;
            }).toList();
    }
}
