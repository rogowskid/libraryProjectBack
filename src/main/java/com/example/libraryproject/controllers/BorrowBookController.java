package com.example.libraryproject.controllers;

import com.example.libraryproject.services.BorrowBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/borrow")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    public BorrowBookController(BorrowBookService borrowBookService) {
        this.borrowBookService = borrowBookService;
    }

    @GetMapping("/book/{idBook}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addBook(@PathVariable String idBook)
    {
        return borrowBookService.addBorrowBook(Long.valueOf(idBook));
    }

}
