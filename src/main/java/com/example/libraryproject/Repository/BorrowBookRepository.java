package com.example.libraryproject.Repository;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {

    Boolean existsBookBorrowByBook(Book book);

}
