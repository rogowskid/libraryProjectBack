package com.example.libraryproject.services;

import com.example.libraryproject.Models.ERole;
import com.example.libraryproject.Models.UStatus;
import com.example.libraryproject.Models.User;
import com.example.libraryproject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public void changeStatus(Long idUser)
    {
        Optional<User> user = userRepository.findById(idUser);
        if (user.get().getStatus().equals(UStatus.STATUS_ACTIVE)) {
            user.get().setStatus(UStatus.STATUS_INACTIVE);

        } else {
            user.get().setStatus(UStatus.STATUS_ACTIVE);

        }

        userRepository.save(user.get());


    }


}
