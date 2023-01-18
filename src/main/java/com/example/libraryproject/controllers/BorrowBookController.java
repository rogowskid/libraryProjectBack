package com.example.libraryproject.controllers;

import com.example.libraryproject.Models.BorrowBook;
import com.example.libraryproject.services.BorrowBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/borrow")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    public BorrowBookController(BorrowBookService borrowBookService) {
        this.borrowBookService = borrowBookService;
    }

    @GetMapping("/book/{idBook}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addBook(@PathVariable String idBook) {
        return borrowBookService.addBorrowBook(Long.valueOf(idBook));
    }

    @GetMapping("/book/reservation/{idBook}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> reservationBook(@PathVariable String idBook) {
        return borrowBookService.getReservationBook(Long.valueOf(idBook));
    }

    @GetMapping("/book/borrowed/{idUser}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public List<?> getBorrowedBook(@PathVariable Long idUser) {
        return borrowBookService.getBorrowedBooks(idUser);
    }

    @GetMapping("/book/return/{idBook}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> returnBook(@PathVariable Long idBook) {
        return borrowBookService.getReturnBook(idBook);
    }

    @GetMapping("book/accept/{idBook}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> acceptBorrowBook(@PathVariable Long idBook) {
        return borrowBookService.getAcceptBorrowBookOnline(idBook);
    }

    @GetMapping("book/accept/reservation/{idBook}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> acceptReservationBook(@PathVariable Long idBook) {
        return borrowBookService.getAcceptBookTradicional(idBook);
    }

    @GetMapping("book/cancel/{idBook}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> cancelBorrowBook(@PathVariable Long idBook) {
        return borrowBookService.getCancelBorrowBook(idBook);
    }

    @GetMapping("/book/waitingbook")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<BorrowBook> getWaitingBooks() {
        return borrowBookService.getWaitingBooks();
    }

    @GetMapping("/book/return/extend/{idBook}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> extendReturnBook(@PathVariable Long idBook) {
        return borrowBookService.extendReturnBook(idBook);
    }

    @GetMapping("/book/return/request/{idBook}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> requestReturnBook(@PathVariable Long idBook) {
        return borrowBookService.canReturnBook(idBook);

    }


    @GetMapping("/book/allborrowedbook")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<BorrowBook> allBorrowedBook() {
        return borrowBookService.getBorrowedBooks();
    }

}
