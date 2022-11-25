package com.example.libraryproject.services;

import com.example.libraryproject.Models.UStatus;
import com.example.libraryproject.Models.User;
import com.example.libraryproject.Repository.UserRepository;
import com.example.libraryproject.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public List<User> getUsersByRole(String roleName) {
        return userRepository.findAll().stream().filter(user -> user.getRole().getName().toString().equals(roleName))
                .collect(Collectors.toList());
    }

    public void changeStatus(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.get().getStatus().equals(UStatus.STATUS_ACTIVE)) {
            user.get().setStatus(UStatus.STATUS_INACTIVE);

        } else {
            user.get().setStatus(UStatus.STATUS_ACTIVE);

        }

        userRepository.save(user.get());


    }

    public User getUser(Long idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    public ResponseEntity updateUser(User user) {
        userRepository.findById(user.getId()).ifPresent(userInRepository ->
                {
                    userInRepository.setEmail(user.getEmail());
                    userInRepository.setUserFirstName(user.getUserFirstName());
                    userInRepository.setUserSecondName(user.getUserSecondName());

                    userRepository.save(userInRepository);
                }

        );


        return ResponseEntity.ok(new MessageResponse("Poprawnie zaaktualizowałeś użytkownika"));
    }


}
