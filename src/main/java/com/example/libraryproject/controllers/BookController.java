package com.example.libraryproject.controllers;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BookController {


    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    /**
     * Zwraca książki bez podziału na wypożyczenia
     * */
    @GetMapping("/allbooks")
    @PreAuthorize("hasRole('USER')")
    public List<Book> getBooks()
    {
        return bookService.getBooks();
    }

    @GetMapping("/books")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<Book> getAccessBooks()
    {
        return bookService.getBooks(0);
    }
    @PostMapping("/addbook")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> addBook(@RequestBody Book book)
    {
        return bookService.addBook(book);
    }






}
