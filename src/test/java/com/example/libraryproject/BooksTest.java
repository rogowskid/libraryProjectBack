package com.example.libraryproject;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.BookCategory;
import com.example.libraryproject.Repository.BookCategoryRepository;
import com.example.libraryproject.Repository.BookRepository;
import com.example.libraryproject.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Test
    @WithMockUser(username = "moderator", roles = {"MODERATOR"})
    void addBook() {
        if (bookCategoryRepository.findByCategoryName("Powieść") == null)
            bookCategoryRepository.save(new BookCategory("Akcja"));

        Book book = new Book("Testowa ksiazka", "Testowy tytul", 1931,
                "8205236266999", bookCategoryRepository.findByCategoryName("Powieść"));
        ResponseEntity<?> responseEntity = bookService.addBook(book);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "moderator", roles = {"MODERATOR"})
    void deleteBook() {
        if (bookCategoryRepository.findByCategoryName("Powieść") == null)
            bookCategoryRepository.save(new BookCategory("Akcja"));

        Book book = new Book("Testowa ksiazka", "Testowy tytul", 1931,
                "8205236266999", bookCategoryRepository.findByCategoryName("Powieść"));
        ResponseEntity<?> responseEntity = bookService.addBook(book);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        ResponseEntity<?> deleteBookEntity = bookService.deleteBook(book);

        assertTrue(deleteBookEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @WithMockUser()
    void getBook() {
        Book bookRepo = bookRepository.findById(1L).orElse(null);
        assertNotNull(bookRepo);
        List<Book> filterBooks = bookService.getFilterBooks(bookRepo.getBookName());
        Book first = filterBooks.stream().filter(bookList -> bookList.getISBN().equals(bookRepo.getISBN())).findFirst().get();

        assertEquals(bookRepo.getIdBook(), first.getIdBook());
    }

    @Test
    @WithMockUser(username = "moderator", roles = {"MODERATOR"})
    void addCopyOfBook() {
        Book bookRepo = bookRepository.findById(1L).orElse(null);
        assertNotNull(bookRepo);

        ResponseEntity<?> responseEntity = bookService.addBook(bookRepo);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

    }

}
