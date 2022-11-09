package com.example.libraryproject.services;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.BorrowBook;
import com.example.libraryproject.Models.UStatus;
import com.example.libraryproject.Models.User;
import com.example.libraryproject.Repository.BookRepository;
import com.example.libraryproject.Repository.BorrowBookRepository;
import com.example.libraryproject.Repository.UserRepository;
import com.example.libraryproject.config.SessionComponent;
import com.example.libraryproject.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


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
                    .body(new MessageResponse("Taka książka nie istnieje!"));

        if (book.getCapacity() < 1)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Brak dostępnych egzemplarzy"));

        User user = userRepository.findByUsername(sessionComponent.getSessionUserLogin()).orElseThrow(() -> null);
        if (user != null) {
            borrowBookRepository.save(new BorrowBook(user, book, LocalDate.now(), LocalDate.now().plusWeeks(2)));
            bookRepository.setBorrowBOok(1, book.getIdBook());
        }

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Brawo! Udało Ci się wypożyczyć książke."));

    }

    @Scheduled(cron = "0 0 * * * *")
    void checkBorrowBook() {

        List<BorrowBook> bookBorrowStream = borrowBookRepository.findAll()
                .stream().filter(book -> book.getDateReturnBook().isBefore(LocalDate.now())).toList();

        bookBorrowStream.forEach(bookBorrow ->
                userRepository.findById(bookBorrow.getUser().getId()).ifPresent(user -> {
                    user.setStatus(UStatus.STATUS_INACTIVE);
                    userRepository.save(user);
                })
        );

    }


}