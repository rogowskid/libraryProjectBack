package com.example.libraryproject.Models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "borrowBook")
public class BorrowBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idBookBorrow;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User user;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idBook")
    private Book book;
    @NotNull
    private LocalDate dateBorrowBook;

    private LocalDate dateReturnBook;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BStatus status;


    public BorrowBook(User user, Book book, LocalDate dateBorrowBook, LocalDate dateReturnBook) {
        this.user = user;
        this.book = book;
        this.dateBorrowBook = dateBorrowBook;
        this.dateReturnBook = dateReturnBook;
        this.status = BStatus.WYPOZYCZENIE_ELEKTRONICZNE;
    }

    public BorrowBook(User user, Book book, LocalDate dateBorrowBook, LocalDate dateReturnBook, BStatus bStatus) {
        this.user = user;
        this.book = book;
        this.dateBorrowBook = dateBorrowBook;
        this.dateReturnBook = dateReturnBook;
        this.status = bStatus;
    }

    public BStatus getStatus() {
        return status;
    }

    public void setStatus(BStatus status) {
        this.status = status;
    }

    public BorrowBook() {

    }

    public long getIdBookBorrow() {
        return idBookBorrow;
    }

    public void setIdBookBorrow(long idBookBorrow) {
        this.idBookBorrow = idBookBorrow;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getDateBorrowBook() {
        return dateBorrowBook;
    }

    public void setDateBorrowBook(LocalDate dateBorrowBook) {
        this.dateBorrowBook = dateBorrowBook;
    }

    public LocalDate getDateReturnBook() {
        return dateReturnBook;
    }

    public void setDateReturnBook(LocalDate dateReturnBook) {
        this.dateReturnBook = dateReturnBook;
    }
}
