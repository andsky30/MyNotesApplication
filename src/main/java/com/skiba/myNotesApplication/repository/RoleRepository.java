package com.skiba.myNotesApplication.repository;

import com.skiba.myNotesApplication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String role);

    Set<Role> findByUsers_Email(String email);

}