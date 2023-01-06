package com.example.libraryproject.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 30)
    private String userFirstName;

    @NotBlank
    @Size(max =40 )
    private String userSecondName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRole")
    private Role role;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<BorrowBook> booksBorrowList;

    @JsonIgnore
    public List<BorrowBook> getBooksBorrowList() {
        return booksBorrowList;
    }

    @JsonIgnore
    public void setBooksBorrowList(List<BorrowBook> booksBorrowList) {
        this.booksBorrowList = booksBorrowList;
    }

    public User() {
    }

    public UStatus getStatus() {
        return status;
    }

    public void setStatus(UStatus status) {
        this.status = status;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = UStatus.STATUS_AKTYWNY;
    }

    public User(String username, String email, String password, String userFirstName, String userSecondName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userFirstName = userFirstName;
        this.userSecondName = userSecondName;
        this.status = UStatus.STATUS_AKTYWNY;
    }

    public User(String username, String email, String password, String userFirstName, String userSecondName,
                UStatus status, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userFirstName = userFirstName;
        this.userSecondName = userSecondName;
        this.status = status;
        this.role = role;
    }

    public User(String username, String email, String password, UStatus status, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserSecondName() {
        return userSecondName;
    }

    public void setUserSecondName(String userSecondName) {
        this.userSecondName = userSecondName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
