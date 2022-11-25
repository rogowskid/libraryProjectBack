package com.example.libraryproject.Repository;

import com.example.libraryproject.Models.BStatus;
import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.BorrowBook;
import com.example.libraryproject.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {

    Boolean existsBookBorrowByBook(Book book);

    List<BorrowBook> findAllByUser(User user);

    BorrowBook findByBook(Book book);

    List<BorrowBook> findAllByStatus(BStatus status);
}
