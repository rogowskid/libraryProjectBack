package com.example.libraryproject.services;

import com.example.libraryproject.Models.ERole;
import com.example.libraryproject.Models.Role;
import com.example.libraryproject.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    void checkExistsRoles()
    {

            if(roleRepository.findByName(ERole.ROLE_USER).isEmpty())
                roleRepository.save(new Role(ERole.ROLE_USER));

            if(roleRepository.findByName(ERole.ROLE_MODERATOR).isEmpty())
                roleRepository.save(new Role(ERole.ROLE_MODERATOR));

            if(roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty())
                roleRepository.save(new Role(ERole.ROLE_ADMIN));

    }


}
