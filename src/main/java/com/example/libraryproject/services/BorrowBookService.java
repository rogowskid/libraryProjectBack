package com.example.libraryproject.services;

import com.example.libraryproject.Models.*;
import com.example.libraryproject.Repository.BookRepository;
import com.example.libraryproject.Repository.BorrowBookRepository;
import com.example.libraryproject.Repository.UserRepository;
import com.example.libraryproject.config.SessionComponent;
import com.example.libraryproject.payload.response.MessageResponse;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


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
            bookRepository.setBorrowBook(1, book.getIdBook());
        }

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Brawo! Udało Ci się wypożyczyć książke."));

    }

    public Long getBorrowsCount(Long idUser) {
        User user = userRepository.findById(idUser).get();

        List<BorrowBook> all = borrowBookRepository.findAll();

        long count = all.stream().filter(book -> book.getUser().equals(user) && !book.getStatus().toString()
                .equals(BStatus.ANULOWANE.name())).count();

        return count;

    }

    public List<BorrowBook> getBorrowedBooks(Long idUser) {

        List<BorrowBook> __borrow = new LinkedList<>();
        List<BorrowBook> borrowedBookByUser = borrowBookRepository.findAllByUser(userRepository.findById(idUser).get())
                .stream().filter(borrowBook -> Objects.equals(borrowBook.getUser().getId(), idUser))
                .filter(borrowBook -> !borrowBook.getStatus().toString().equals(BStatus.ANULOWANE.name())).toList();

        for (BorrowBook borrowBook : borrowedBookByUser) {
            if (Hibernate.unproxy(borrowBook.getBook()) instanceof Book book) {
                borrowBook.setBook(book);
                __borrow.add(borrowBook);
            }

        }


        return __borrow;
    }

    public ResponseEntity<?> getReturnBook(Long idBook) {
        Book book = bookRepository.findById(idBook).orElse(null);
        if (book == null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Wystąpił błąd"));


        BorrowBook byBook = borrowBookRepository.findByBook(book);
        if (byBook == null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Wystąpił błąd"));

        if(byBook.getUser() != null)
            byBook.setUser((User) Hibernate.unproxy(byBook.getUser()));



        borrowBookRepository.delete(byBook);
        book.setCapacity(book.getCapacity() + 1);
        return ResponseEntity
                .ok()
                .body(new MessageResponse("Pomyślnie zwrócono książke"));
    }

    public ResponseEntity<?> getAcceptBorrowBook(Long idBook)
    {
        Book book = bookRepository.findById(idBook).orElse(null);
        if(book == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Coś poszło nie tak."));

        BorrowBook byBook = borrowBookRepository.findByBook(book);
        byBook.setStatus(BStatus.ZREALIZOWANE);

        return ResponseEntity.ok().body(new MessageResponse("Pomyślnie zmieniono status."));
    }

    public ResponseEntity<?> getCancelBorrowBook(Long idBook)
    {
        Book book = bookRepository.findById(idBook).orElse(null);
        if(book == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Coś poszło nie tak."));

        BorrowBook byBook = borrowBookRepository.findByBook(book);
        borrowBookRepository.delete(byBook);

        return ResponseEntity.ok().body(new MessageResponse("Pomyślnie anulowano wypożyczenie."));

    }
    public List<BorrowBook> getWaitingBooks()
    {


        return borrowBookRepository.findAllByStatus(BStatus.W_OCZEKIWANIU);
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