package com.example.libraryproject.Models;


import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "books",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "ISBN")
        })
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBook;

    @NotBlank
    @Size(max = 40)
    private String bookName;

    @NotBlank
    @Size(max = 30)
    private String author;

    @NotNull
    @Range(min=1000, max = 9999)
    private int yearOfPublication;

    @NotBlank
    @Size(max = 13)
    private String ISBN;

    @NotNull
    @Range(min=0, max=4000)
    private int capacity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<BookBorrow> booksBorrowList;

    public Book(String bookName, String author, int yearOfPublication, String ISBN) {
        this.bookName = bookName;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.ISBN = ISBN;
        this.capacity = 1;
    }

    public Book(String bookName, String author, int yearOfPublication, String ISBN, int capacity) {
        this.bookName = bookName;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.ISBN = ISBN;
        this.capacity = capacity;
    }

    public Book() {

    }

    public long getIdBook() {
        return idBook;
    }

    public void setIdBook(long idBook) {
        this.idBook = idBook;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
