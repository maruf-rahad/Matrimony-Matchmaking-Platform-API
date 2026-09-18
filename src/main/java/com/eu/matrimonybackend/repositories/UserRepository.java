package com.eu.matrimonybackend.repositories;

import com.eu.matrimonybackend.enums.Role;
import com.eu.matrimonybackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByProfileId(Long profileId);
    List<User> findByRolesContaining(Role role);
    long countByRolesContaining(Role role);
}