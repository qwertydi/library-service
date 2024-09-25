package com.dmsc.libraryserviceapi.service.books;

import com.dmsc.libraryserviceapi.entity.AuthorEntity;
import com.dmsc.libraryserviceapi.entity.LanguagesEntity;
import com.dmsc.libraryserviceapi.entity.LocalBookEntity;
import com.dmsc.libraryserviceapi.exception.LibraryInvalidDataException;
import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.model.book.CreateBookRequest;
import com.dmsc.libraryserviceapi.repository.LocalBookRepository;
import com.dmsc.libraryserviceapi.service.authors.LocalAuthorsService;
import com.dmsc.libraryserviceapi.service.language.LocalLanguageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocalBookServiceImpl implements LocalBookService {

    private final LocalBookRepository localBookRepository;
    private final LocalAuthorsService localAuthorsService;
    private final LocalLanguageService localLanguageService;

    public LocalBookServiceImpl(LocalBookRepository localBookRepository,
                                LocalAuthorsService localAuthorsService,
                                LocalLanguageService localLanguageService) {
        this.localBookRepository = localBookRepository;
        this.localAuthorsService = localAuthorsService;
        this.localLanguageService = localLanguageService;
    }

    @Transactional
    @Override
    public Optional<BookResponse> addNewBook(CreateBookRequest request) {
        List<LocalBookEntity> byTitle = localBookRepository.findAllByTitleEqualsIgnoreCase(request.getTitle());
        if (!CollectionUtils.isEmpty(byTitle)) {
            throw new LibraryInvalidDataException("Book already exists!", HttpStatus.CONFLICT);
        }

        List<AuthorEntity> authorsList = request.getAuthors()
            .stream()
            .filter(Objects::nonNull)
            .map(localAuthorsService::findOrCreateAuthorByName).toList();
        List<LanguagesEntity> languagesList = request.getLanguages()
            .stream()
            .filter(Objects::nonNull)
            .map(localLanguageService::findOrCreateLanguageByName).toList();
        LocalBookEntity save = localBookRepository.save(
            LocalBookEntity.builder()
                .publishYear(Integer.valueOf(request.getPublishYear()))
                .title(request.getTitle())
                .languages(languagesList)
                .authors(authorsList)
                .build()
        );

        return Optional.of(BookResponse.fromLocalBook(save));
    }

    @Override
    public List<BookResponse> searchAllBooks() {
        return localBookRepository.findAll()
            .stream()
            .map(BookResponse::fromLocalBook)
            .toList();
    }

    @Override
    public Optional<BookResponse> searchBookById(String id) {
        return localBookRepository.findById(Long.valueOf(id))
            .map(BookResponse::fromLocalBook);
    }

    @Override
    public List<BookResponse> searchBookByTitle(String title) {
        return localBookRepository.findAllByTitleEqualsIgnoreCase(title)
            .stream()
            .map(BookResponse::fromLocalBook)
            .toList();
    }
}
