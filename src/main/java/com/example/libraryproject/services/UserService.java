package com.example.libraryproject.services;

import com.example.libraryproject.Models.User;
import com.example.libraryproject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public List<User> getUsersByRole(String roleName) {
        return userRepository.findAll().stream().filter(user -> user.getRoles()
                .stream().allMatch(role -> role.getName().toString()
                        .equals(roleName))).collect(Collectors.toList());
    }


}
