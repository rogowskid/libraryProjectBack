package com.example.libraryproject;

import com.example.libraryproject.Models.ERole;
import com.example.libraryproject.Models.UStatus;
import com.example.libraryproject.Models.User;
import com.example.libraryproject.Repository.RoleRepository;
import com.example.libraryproject.Repository.UserRepository;
import com.example.libraryproject.controllers.AuthController;
import com.example.libraryproject.payload.request.LoginRequest;
import com.example.libraryproject.payload.request.SignupRequest;
import com.example.libraryproject.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class UsersTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthController authController;

    @Test
    @WithAnonymousUser
    public void addUser() {
        User user = new User("test", "test@wp.pl", passwordEncoder.encode("123456"), "Test", "Testo1");
        userRepository.save(user);

        User user1 = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(user1);
        userRepository.delete(user1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addModeratorRole() {

        User user = new User("test", "test@wp.pl", passwordEncoder.encode("123456"), "Test", "Testo1",
                UStatus.STATUS_ACTIVE, roleRepository.findByName(ERole.ROLE_USER).orElseThrow());

        userRepository.save(user);
        ResponseEntity<?> responseEntity = userService.setModeratorMode(user.getId());

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        userRepository.delete(user);
    }

    @Test
    @WithMockUser(username = "moderator", roles = {"MODERATOR"})
    public void deleteUser() {
        User user = new User("test", "test@wp.pl", passwordEncoder.encode("123456"), "Test", "Testo1",
                UStatus.STATUS_ACTIVE, roleRepository.findByName(ERole.ROLE_USER).orElseThrow());

        userRepository.save(user);
        ResponseEntity<?> responseEntity = userService.deleteUser(user.getId());
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    public void changeStatus() {
        User user = new User("test", "test@wp.pl", passwordEncoder.encode("123456"), "Test", "Testo1",
                UStatus.STATUS_ACTIVE, roleRepository.findByName(ERole.ROLE_USER).orElseThrow());
        userRepository.save(user);
        userService.changeStatus(user.getId());
        assertEquals(userRepository.findById(user.getId()).get().getStatus(), UStatus.STATUS_INACTIVE);
        userRepository.delete(user);
    }

    @Test
    @WithAnonymousUser
    public void loginBadCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("123");
        loginRequest.setUsername("test");

        assertThrowsExactly(BadCredentialsException.class, () -> authController.authenticateUser(loginRequest));
    }

    @Test
    public void loginGoodCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("123456");
        loginRequest.setUsername("admin");

        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void registerBadCredentials() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("admin");
        signupRequest.setEmail("test@wp.pl");
        signupRequest.setPassword("43312312");
        signupRequest.setUserFirstName("tes");
        signupRequest.setUserSecondName("2sws");
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertTrue(responseEntity.getStatusCode().is4xxClientError());


    }


}
