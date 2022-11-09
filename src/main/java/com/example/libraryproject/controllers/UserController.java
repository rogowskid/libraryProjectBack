package com.example.libraryproject.controllers;

import com.example.libraryproject.Models.User;
import com.example.libraryproject.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/allusers/{role}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<User> getUsers(@PathVariable String role)
    {
        return userService.getUsersByRole(role);
    }

    @GetMapping("/changestatus/{idUser}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void changeStatusUser(@PathVariable Long idUser)
    {
        userService.changeStatus(idUser);
    }

}
