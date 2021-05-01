package com.api.authentication.authenticationapi.service;

import java.util.List;

import com.api.authentication.authenticationapi.model.ApplicationUser;
import com.api.authentication.authenticationapi.repository.ApplicationUserRepository;
import com.api.authentication.authenticationapi.service.dao.ApplicationUserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService, ApplicationUserDao {

    private ApplicationUserRepository applicationUserRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository,
            PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = applicationUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("Utilisateur %s n'existe pas", username)));

        return user;
    }

    @Override
    public boolean addNewUser(ApplicationUser user) {

        boolean usernameAlreadyExist = countUserByUsername(user.getUsername()) > 0;

        if (usernameAlreadyExist) {
            return false;
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(true);
            user.setAccountNonLocked(true);
            user.setAccountNonExpired(true);
            user.setCredentialsNonExpired(true);
            applicationUserRepository.save(user);
            return true;
        } catch (DataAccessException e) {
            System.out.println("ERROR WHILE SAVING THE USER");
            return false;
        }
    }

    @Override
    public boolean emailAlreadyExist(String email) {
        return applicationUserRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean usernameAlreadyExist(String username) {
        return applicationUserRepository.findByUsername(username).isPresent();
    }

    @Override
    public Integer countUserByUsername(String username) {
        return applicationUserRepository.countUserByUsername(username);
    }
}
