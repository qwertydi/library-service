package com.dmsc.libraryserviceapi.controller;

import com.dmsc.libraryserviceapi.model.book.CreateBookRequest;
import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.service.books.BookManagementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/book")
public class BookController {
    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    private final BookManagementService bookManagementService;

    public BookController(BookManagementService bookManagementService) {
        this.bookManagementService = bookManagementService;
    }

    @GetMapping("{id}")
    public BookResponse getBookById(@PathVariable("id") String id) {
        LOG.info("Book by ID: {}", id);
        return bookManagementService.searchBookById(id).orElse(null);
    }

    @GetMapping("all")
    public List<BookResponse> getAllBooks() {
        LOG.info("Get all books");
        return bookManagementService.searchAllBooks();
    }

    @GetMapping("search")
    public List<BookResponse> searchBookByTitle(@RequestParam("title") String title) {
        LOG.info("Get book by title");
        return bookManagementService.searchBookByTitle(title);
    }

    @PostMapping
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest request) {
        LOG.info("Get all books: {}", request);
        return bookManagementService.createBook(request);
    }
}
