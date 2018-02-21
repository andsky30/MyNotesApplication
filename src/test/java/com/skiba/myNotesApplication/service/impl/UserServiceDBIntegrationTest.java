package com.skiba.myNotesApplication.service.impl;

import com.google.common.collect.Sets;
import com.skiba.myNotesApplication.api.service.UserService;
import com.skiba.myNotesApplication.model.Role;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.RoleRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceDBIntegrationTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;


    @Test
    public void shouldFetchRolesFromService() {
        final Set<Role> roles = userService.findRolesForUser("email@test.pjn");
        assertThat(roles).hasSize(1);
    }

    @Test
    public void shouldSaveUserWithRoles() {
        //given
        Role role = new Role();
        role.setName("PP");
        roleRepository.save(role);

        Role role2 = new Role();
        role.setName("KK");
        roleRepository.save(role2);

        User user = createUser(role, role2);
        User user2 = createUser(role, role2);

        //when
        final User save = userRepository.save(user);
        final User save2 = userRepository.save(user2);

        //then
        assertThat(save).isNotNull();
        final Set<Role> saveRoles = save.getRoles();
        assertThat(saveRoles).isNotNull();
        assertThat(saveRoles).hasSize(2);

        assertThat(save2).isNotNull();
        final Set<Role> saveRoles2 = save2.getRoles();
        assertThat(saveRoles2).isNotNull();
        assertThat(saveRoles2).hasSize(2);

    }

    private User createUser(Role role, Role role2) {
        User user = new User();
        user.setFirstName("aa");
        user.setEmail("a@a.pl");
        user.setRoles(Sets.newHashSet(role, role2));
        return user;
    }
}