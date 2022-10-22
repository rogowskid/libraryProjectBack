package com.example.libraryproject.Repository;

import com.example.libraryproject.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository <Book, Long> {

    @Override
    Optional<Book> findById(Long idBook);

    Optional<Book> findByISBN(String isbn);
    Boolean existsByISBN(String ISBN);

    List<Book> findByBookNameContainsIgnoreCase(String name);
    List<Book> findByCapacityGreaterThan(int capacity);


    @Modifying
    @Transactional
    @Query(value = "update Book set capacity = capacity + ?1 where idBook=?2")
    void addBookCapacity(int capacity, Long idBook);

    @Modifying
    @Transactional
    @Query(value = "update Book set capacity = capacity - ?1 where idBook=?2")
    void setBorrowBOok(int capacity, Long idBook);

}
