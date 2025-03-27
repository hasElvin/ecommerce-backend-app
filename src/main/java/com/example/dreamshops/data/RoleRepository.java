package com.example.dreamshops.data;

import com.example.dreamshops.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Collection<Role> findByName(String name);
}
