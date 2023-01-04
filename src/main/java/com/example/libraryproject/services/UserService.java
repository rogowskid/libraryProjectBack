package com.example.libraryproject.services;

import com.example.libraryproject.Models.ERole;
import com.example.libraryproject.Models.Role;
import com.example.libraryproject.Models.UStatus;
import com.example.libraryproject.Models.User;
import com.example.libraryproject.Repository.RoleRepository;
import com.example.libraryproject.Repository.UserRepository;
import com.example.libraryproject.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostConstruct
    private void getAdminAndModerator() {

        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty())
            roleRepository.save(new Role(ERole.ROLE_ADMIN));

        if (roleRepository.findByName(ERole.ROLE_MODERATOR).isEmpty())
            roleRepository.save(new Role(ERole.ROLE_MODERATOR));

        if (roleRepository.findByName(ERole.ROLE_USER).isEmpty())
            roleRepository.save(new Role(ERole.ROLE_USER));

        if (userRepository.count() == 0) {
            userRepository.save(new User("moderator", "mod@support.com", passwordEncoder.encode("123456"),
                    "Daniel", "Moderatorek", UStatus.STATUS_ACTIVE,
                    roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow()));

            userRepository.save(new User("admin", "admin@support.com", passwordEncoder.encode("123456"),
                    "Daniel", "Adminek", UStatus.STATUS_ACTIVE,
                    roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow()));

            userRepository.save(new User("user", "user@wp.pl", passwordEncoder.encode("123456"),
                    "Daniel", "Userek", UStatus.STATUS_ACTIVE, roleRepository.findByName(ERole.ROLE_USER).orElseThrow()));
        }
    }

    public List<User> getUsersByRole(String[] roleName) {
        List<User> results = new LinkedList<>();

        for (String name : roleName) {
            results.addAll(userRepository.findAll().stream().filter(user ->
                    user.getRole().getName().toString().equals(name)).toList());
        }

        return results;
    }

    public ResponseEntity<?> setModeratorMode(Long idUser) {
        User user = userRepository.findById(idUser).orElse(null);
        if (!user.getBooksBorrowList().isEmpty())
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Użytkownik ma wypożyczone książki. Nie może zostać moderatorem"));

        user.setRole(roleRepository.findByName(ERole.ROLE_MODERATOR).orElse(user.getRole()));

        userRepository.save(user);

        return ResponseEntity
                .ok(new MessageResponse("Poprawienie nadano uprawienia moderatora."));

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

    public ResponseEntity<?> deleteUser(Long idUser) {
        User user = userRepository.findById(idUser).orElse(null);
        if (user == null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Użytkownik o identyfikatorze " + idUser + "nie istnieje w bazie."));

        userRepository.delete(user);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Poprawnie usunięto użytklownika o loginie " + user.getUsername()));
    }


    public ResponseEntity<?> updateUserPassword(Map<String, String> json) {

        User user = userRepository.findByUsername((json.get("login"))).get();


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(json.get("login"), json.get("nowPassword"))).isAuthenticated();
        } catch (AuthenticationException e) {
            ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Aktualne hasło zostało podane błędne. Spróbuj ponownie."));
        }


        user.setPassword(passwordEncoder.encode(json.get("password")));


        return ResponseEntity
                .ok()
                .body(new MessageResponse("Pomyślnie udało ci się zmienić hasło"));

    }
}
