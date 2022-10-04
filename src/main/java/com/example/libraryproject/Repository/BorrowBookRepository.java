package com.example.libraryproject.Repository;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.BookBorrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowBookRepository extends JpaRepository <BookBorrow, Long> {

    Boolean existsBookBorrowByBook(Book book);

}
