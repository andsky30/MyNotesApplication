package com.skiba.myNotesApplication.api.service;

import com.skiba.myNotesApplication.api.dto.UserCreation;
import com.skiba.myNotesApplication.api.dto.UserDisplay;
import com.skiba.myNotesApplication.model.Role;
import com.skiba.myNotesApplication.model.User;

import java.util.Set;

public interface UserService  {

    Set<UserDisplay> getAllUsers();

    UserDisplay getSingleUserDisplayByEmail(String email);

    UserDisplay getSingleUserDisplayById(Long userId);

    User findUserByEmail(String email);

    Set<Role> findRolesForUser(String email);

    UserDisplay addUser(UserCreation userCreation);

    void removeUserById(Long userId);

    void removeUserByEmail(String email);

    UserDisplay updateUser(UserCreation userCreation, Long userId);

}
