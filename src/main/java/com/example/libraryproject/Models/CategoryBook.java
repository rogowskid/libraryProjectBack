package com.example.libraryproject.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="category_book")
public class CategoryBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCategoryBook;

    @NotBlank
    @Size(max = 40)
    private String categoryName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "categoryBook")
    private List<Book> book;

    public CategoryBook(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryBook() {

    }



}
