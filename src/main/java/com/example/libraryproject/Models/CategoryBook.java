package com.example.libraryproject.Models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "category_book")
public class CategoryBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCategoryBook;

    @NotBlank
    @Size(max = 40)
    private String categoryName;

    @OneToMany(mappedBy = "categoryBook")
    private List<Book> book;

    public CategoryBook(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryBook() {

    }

    public long getIdCategoryBook() {
        return idCategoryBook;
    }

    public void setIdCategoryBook(long idCategoryBook) {
        this.idCategoryBook = idCategoryBook;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
