package com.example.libraryproject.config;

import com.example.libraryproject.config.services.MyUserDetailsService;
import com.example.libraryproject.config.services.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionComponent {

    public String getSessionUserLogin()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl)
            return ((UserDetailsImpl)principal).getUsername();
        else
            return principal.toString();
    }

}
