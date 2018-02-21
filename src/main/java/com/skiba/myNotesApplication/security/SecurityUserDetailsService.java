package com.skiba.myNotesApplication.security;

import com.skiba.myNotesApplication.api.service.UserService;
import com.skiba.myNotesApplication.model.Role;
import com.skiba.myNotesApplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public SecurityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userService.findUserByEmail(email);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(extractRolesFromUser(user))
                .build();
    }

    private String[] extractRolesFromUser(User user) {
        return
                userService.findRolesForUser(user.getEmail())
                        .stream()
                        .map(Role::getName)
                        .toArray(String[]::new);
    }
}