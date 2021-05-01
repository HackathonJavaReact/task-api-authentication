package com.api.authentication.authenticationapi.repository;

import java.util.Optional;

import com.api.authentication.authenticationapi.model.ApplicationUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {
    public Optional<ApplicationUser> findByUsername(String username);

    public Optional<ApplicationUser> findByEmail(String email);

    public Integer countUserByUsername(String username);
}
