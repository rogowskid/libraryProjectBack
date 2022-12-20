package com.example.libraryproject;

import com.example.libraryproject.controllers.AuthController;
import com.example.libraryproject.payload.request.LoginRequest;
import com.example.libraryproject.services.BorrowBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class BorrowBooksTest {
    @Autowired
    private BorrowBookService borrowBookService;

    @Autowired
    private AuthController authController;


    @Test
    public void borrowBook() {
        createUser("user");
        ResponseEntity<?> responseEntity = borrowBookService.addBorrowBook(2L);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

    }

    @Test
    public void returnBook() {
        //then
        createUser("user");
        borrowBookService.addBorrowBook(1L);
        createUser("moderator");
        ResponseEntity<?> acceptBorrowBook = borrowBookService.getAcceptBorrowBook(1L);
        assertTrue(acceptBorrowBook.getStatusCode().is2xxSuccessful());
        ResponseEntity<?> returnBook = borrowBookService.getReturnBook(1L);
        assertTrue(returnBook.getStatusCode().is2xxSuccessful());
    }


    private void createUser(String login) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(login);
        loginRequest.setPassword("123456");

        authController.authenticateUser(loginRequest);
    }

}
