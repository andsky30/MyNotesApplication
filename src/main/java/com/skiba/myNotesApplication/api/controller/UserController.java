package com.skiba.myNotesApplication.api.controller;

import com.skiba.myNotesApplication.api.dto.UserCreation;
import com.skiba.myNotesApplication.api.dto.UserDisplay;
import com.skiba.myNotesApplication.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.Set;

@RestController
public class UserController {

    public static final String MESSAGE_AFTER_USER_BY_ID_DELETION = "User with ID: %d has been deleted successfully!!!";
    public static final String MESSAGE_AFTER_USER_BY_EMAIL_DELETION = "User with e-mail: %s has been deleted successfully!!!";

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured(value = "ROLE_ADMIN")
    @GetMapping(value = "/api/users")
    public ResponseEntity<Set<UserDisplay>> getAllUsers() {

        Set<UserDisplay> allUsers = userService.getAllUsers();

        return ResponseEntity.ok(allUsers);
    }

    @Secured(value = "ROLE_ADMIN")
    @GetMapping(value = "/api/users/{userId}")
    public ResponseEntity<UserDisplay> getSingleUser(@PathVariable Long userId){

        UserDisplay singleUser = userService.getSingleUserDisplayById(userId);

        return ResponseEntity.ok(singleUser);
    }

    @GetMapping(value = "/api/users/account")
    public ResponseEntity<UserDisplay> getSingleUser(Principal principal){

        UserDisplay singleUser = userService.getSingleUserDisplayByEmail(principal.getName());

        return ResponseEntity.ok(singleUser);
    }

    @PostMapping(value = "/api/users")
    public ResponseEntity<UserDisplay> addUser(@Valid @RequestBody UserCreation userCreation){

        UserDisplay saved = userService.addUser(userCreation);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @Secured(value = "ROLE_ADMIN")
    @DeleteMapping(value = "/api/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){

        userService.removeUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format(MESSAGE_AFTER_USER_BY_ID_DELETION, userId));
    }

    @DeleteMapping(value = "/api/users")
    public ResponseEntity<String> deleteUser(Principal principal){

        String email = principal.getName();

        userService.removeUserByEmail(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format(MESSAGE_AFTER_USER_BY_EMAIL_DELETION, email));
    }
}
