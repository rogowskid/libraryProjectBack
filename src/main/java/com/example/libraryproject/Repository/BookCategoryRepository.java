package com.example.libraryproject.Repository;

import com.example.libraryproject.Models.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    public BookCategory findByCategoryName(String name);

}
