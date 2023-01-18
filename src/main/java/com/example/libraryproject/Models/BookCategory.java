package com.example.libraryproject.Models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "book_category")
public class BookCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBookCategory;

    @NotBlank
    @Size(max = 40)
    private String categoryName;

    @OneToMany(mappedBy = "bookCategory")
    private List<Book> book;

    public BookCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public BookCategory() {

    }

    public long getIdBookCategory() {
        return idBookCategory;
    }

    public void setIdBookCategory(long idBookCategory) {
        this.idBookCategory = idBookCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
