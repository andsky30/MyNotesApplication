package com.skiba.myNotesApplication.service.impl;

import com.skiba.myNotesApplication.api.dto.UserCreation;
import com.skiba.myNotesApplication.api.dto.UserDisplay;
import com.skiba.myNotesApplication.api.service.UserService;
import com.skiba.myNotesApplication.model.Role;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.RoleRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.service.mapper.UserCreationToUserMapper;
import com.skiba.myNotesApplication.service.mapper.UserToUserDisplayMapper;
import com.skiba.myNotesApplication.validation.UserByIdNotFoundException;
import com.skiba.myNotesApplication.validation.UserByEmailNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserToUserDisplayMapper userToUserDisplayMapper;
    UserCreationToUserMapper userCreationToUserMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserToUserDisplayMapper userToUserDisplayMapper,
                           UserCreationToUserMapper userCreationToUserMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userToUserDisplayMapper = userToUserDisplayMapper;
        this.userCreationToUserMapper = userCreationToUserMapper;
    }

    @Override
    public Set<UserDisplay> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userToUserDisplayMapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDisplay getSingleUserDisplayByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else {
            return userToUserDisplayMapper.map(user);
        }
    }

    @Override
    public UserDisplay getSingleUserDisplayById(Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserByIdNotFoundException(userId);
        } else {
            return userToUserDisplayMapper.map(user);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else
            return user;
    }

    @Override
    public Set<Role> findRolesForUser(String email) {
        return roleRepository.findByUsers_Email(email);
    }

    @Override
    public UserDisplay addUser(UserCreation userCreation) {
        User user = userCreationToUserMapper.map(userCreation);
        User savedUser = userRepository.save(user);

        return userToUserDisplayMapper.map(savedUser);
    }

    @Override
    public void removeUserById(Long userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new UserByIdNotFoundException(userId);
        } else {
            userRepository.delete(userId);
        }
    }

    @Override
    public void removeUserByEmail(String email) {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else {
            userRepository.deleteUserByEmail(email);
        }
    }


    @Override
    public UserDisplay updateUser(UserCreation userCreation, Long userId) {
        return null;
    }

}
