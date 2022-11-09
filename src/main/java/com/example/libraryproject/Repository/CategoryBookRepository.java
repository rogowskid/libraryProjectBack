package com.example.libraryproject.Repository;

import com.example.libraryproject.Models.CategoryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryBookRepository extends JpaRepository<CategoryBook, Long> {

    public CategoryBook findByCategoryName(String name);

}
