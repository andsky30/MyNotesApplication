package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.UserDisplay;
import com.skiba.myNotesApplication.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserToUserDisplayMapper {

    public UserDisplay map(User user){
        UserDisplay userDisplay = new UserDisplay();
        userDisplay.setId(user.getId());
        userDisplay.setEmail(user.getEmail());
        userDisplay.setFirstName(user.getFirstName());
        userDisplay.setLastName(user.getLastName());
        userDisplay.setRoles(user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet()));

        return userDisplay;
    }
}
