package com.orbit.repository;

import com.orbit.models.auth.EnumRole;
import com.orbit.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(EnumRole name);
}
