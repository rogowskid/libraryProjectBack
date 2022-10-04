package com.example.libraryproject.Models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "bookBorrow")
public class BookBorrow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idBookBorrow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBook")
    private Book book;

    @NotNull
    private LocalDate dateBorrowBook;

    private LocalDate dateReturnBook;


    public BookBorrow(User user, Book book, LocalDate dateBorrowBook, LocalDate dateReturnBook) {
        this.user = user;
        this.book = book;
        this.dateBorrowBook = dateBorrowBook;
        this.dateReturnBook = dateReturnBook;
    }


    public BookBorrow() {

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
