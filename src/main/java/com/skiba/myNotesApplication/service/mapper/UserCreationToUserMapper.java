package com.skiba.myNotesApplication.service.mapper;

import com.google.common.collect.Sets;
import com.skiba.myNotesApplication.api.dto.UserCreation;
import com.skiba.myNotesApplication.model.Role;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.skiba.myNotesApplication.data.DataLoader.ROLE_USER;

@Component
public class UserCreationToUserMapper {

    @Autowired
    RoleRepository roleRepository;

    public User map(UserCreation userCreation){
        User user = new User();
        user.setEmail(userCreation.getEmail());
        user.setPassword(userCreation.getPassword());
        user.setFirstName(userCreation.getFirstName());
        user.setLastName(userCreation.getLastName());

        Role role = roleRepository.findByName(ROLE_USER);
        user.setRoles(Sets.newHashSet(role));
        return user;
    }
}
