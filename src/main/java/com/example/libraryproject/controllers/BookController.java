package com.example.libraryproject.controllers;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.CategoryBook;
import com.example.libraryproject.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public List<Book> getBooks()
    {
        return bookService.getBooks();
    }

    @GetMapping("/filterbook/{bookName}")
    public List<Book> getFilterBooks(@PathVariable String bookName ){

        if (bookName.isEmpty())
            return bookService.getBooks(0);

        return bookService.getFilterBooks(bookName);

    }

    @GetMapping("/books")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Book> getAccessBooks() {
        return bookService.getBooks(0);
    }

    @PostMapping("/addbook")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PostMapping("/uploadimage")
    @PreAuthorize("hasRole('MODERATOR')")
    public void addBookImage(@RequestParam("image") MultipartFile image) {
        bookService.addImageBook(image);
    }

    @PostMapping("/uploadbook")
    @PreAuthorize("hasRole('MODERATOR')")
    public void addEBook(@RequestParam("book") MultipartFile book) {
        bookService.addEBook(book);
    }

    @GetMapping("/book/{idbook}")
    public Book getBook(@PathVariable Long idbook) {
        return bookService.getBook(idbook);
    }

    @GetMapping("/getallcategories")
    public List<CategoryBook> getCategoriesBook() {
        return bookService.getCategoriesBookName();
    }

    @GetMapping("/book/categories/{categoryName}")
    public List<Book> getBookByCategories(@PathVariable String categoryName)
    {
        return bookService.getBooksByCategory(categoryName);
    }

    @GetMapping("/book/filterbook/{categoryName}/{name}")
    public List<Book> getBookByCategories(@PathVariable String categoryName, @PathVariable String name)
    {
        return bookService.getFilterBooksByCategory(categoryName, name);
    }







}
