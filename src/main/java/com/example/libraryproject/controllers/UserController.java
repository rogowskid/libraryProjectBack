package com.example.libraryproject.controllers;

import com.example.libraryproject.Models.User;
import com.example.libraryproject.services.BorrowBookService;
import com.example.libraryproject.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final BorrowBookService borrowBookService;

    public UserController(UserService userService, BorrowBookService borrowBookService) {
        this.userService = userService;
        this.borrowBookService = borrowBookService;
    }


    @GetMapping("/allusers/{role}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<User> getUsers(@PathVariable String[] role) {
        return userService.getUsersByRole(role);
    }

    @GetMapping("/updaterole/{idUser}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setModeratorRole(@PathVariable Long idUser) {
        return userService.setModeratorMode(idUser);
    }

    @GetMapping("/changestatus/{idUser}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void changeStatusUser(@PathVariable Long idUser) {
        userService.changeStatus(idUser);
    }

    @GetMapping("/user/{idUser}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public User getUser(@PathVariable Long idUser)
    {
        return userService.getUser(idUser);
    }

    @GetMapping("/user/borrowcount/{idUser}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Long getCount(@PathVariable Long idUser) {
        return borrowBookService.getBorrowsCount(idUser);
    }

    @PostMapping("/user/update")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return userService.updateUser(user);

    }

    @PostMapping("/user/update/password")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> json) {
        return userService.updateUserPassword(json);

    }

    @GetMapping("/user/delete/{idUser}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long idUser) {
        return userService.deleteUser(idUser);
    }

}
