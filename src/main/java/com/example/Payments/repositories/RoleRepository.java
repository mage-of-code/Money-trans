package com.example.Payments.repositories;

import com.example.Payments.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByRoleSignature(String name);
}
