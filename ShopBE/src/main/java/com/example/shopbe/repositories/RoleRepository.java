package com.example.shopbe.repositories;

import com.example.shopbe.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String roleName);

}
