package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import com.dmsc.libraryserviceapi.service.hashing.IdentifierHashService;
import com.dmsc.openlibraryapi.model.BookSdk;
import com.dmsc.openlibraryapi.model.SearchSdkRequest;
import com.dmsc.openlibraryapi.model.SearchSdkResponse;
import com.dmsc.openlibraryapi.model.SearchSolrSdkRequest;
import com.dmsc.openlibraryapi.model.SearchTitleSdkRequest;
import com.dmsc.openlibraryapi.sdk.SearchApiSdk;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RemoteBookServiceImpl implements RemoteBookService {

    private final SearchApiSdk searchApiSdk;
    private final ModelMapper modelMapper;
    private final IdentifierHashService identifierHashService;

    public RemoteBookServiceImpl(SearchApiSdk searchApiSdk,
                                 IdentifierHashService identifierHashService) {
        this.searchApiSdk = searchApiSdk;
        this.identifierHashService = identifierHashService;
        this.modelMapper = new ModelMapper();
        PropertyMap<BookSdk, BookResponse> bookSdkToBookResponse = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setAuthors(source.getAuthorName());
                map().setLanguages(source.getLanguage());
                map().setPublishYear(source.getFirstPublishYear());
            }
        };
        this.modelMapper.addMappings(bookSdkToBookResponse);
    }

    @Override
    public Optional<BookResponse> searchBookById(String id) {
        // book key will be used on this query
        SearchSdkRequest request = SearchSolrSdkRequest.builder()
            .query(id)
            .limit(1)
            .build();

        SearchSdkResponse searchSdkResponse = searchApiSdk.searchBooks(request);

        return searchSdkResponse == null ? Optional.empty() :
            searchSdkResponse.getDocs().stream()
                .map(this::getBookResponse).findFirst();
    }

    @Override
    public List<BookResponse> searchBookByTitle(String title) {
        SearchSdkRequest request = SearchTitleSdkRequest.builder()
            .title(title)
            .build();

        SearchSdkResponse searchSdkResponse = searchApiSdk.searchBooks(request);
        return searchSdkResponse.getDocs()
            .stream()
            .map(this::getBookResponse).toList();
    }

    private BookResponse getBookResponse(BookSdk bookSdk) {
        BookResponse bookResponse = modelMapper.map(bookSdk, BookResponse.class);
        bookResponse.setId(identifierHashService.hash(BookSystemEnum.OPENLIBRARY, bookSdk.getKey()));
        return bookResponse;
    }
}
