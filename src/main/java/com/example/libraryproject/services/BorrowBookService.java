package com.example.libraryproject.services;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.BookBorrow;
import com.example.libraryproject.Models.User;
import com.example.libraryproject.Repository.BookRepository;
import com.example.libraryproject.Repository.BorrowBookRepository;
import com.example.libraryproject.Repository.UserRepository;
import com.example.libraryproject.config.SessionComponent;
import com.example.libraryproject.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BorrowBookService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BorrowBookRepository borrowBookRepository;
    @Autowired
    private SessionComponent sessionComponent;

    public ResponseEntity<?> addBorrowBook(Long idBook) {
        Book book = bookRepository.findById(idBook).orElseThrow(() -> null);

        if (book == null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The book does not exist!"));

        if (book.getCapacity() < 1)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("No copy of the book"));

        User user = userRepository.findByUsername(sessionComponent.getSessionUserLogin()).orElseThrow(() -> null);
        if (user != null)
        {
            borrowBookRepository.save(new BookBorrow(user, book, LocalDate.now(), LocalDate.now().plusWeeks(2)));
            bookRepository.setBorrowBOok(1, book.getIdBook());
        }

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Success! You just borrowed a book."));

    }





}