package com.api.authentication.authenticationapi.service.dao;

import java.util.List;

import com.api.authentication.authenticationapi.model.ApplicationUser;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String username);

    public boolean addNewUser(ApplicationUser user);

    public boolean emailAlreadyExist(String email);

    public boolean usernameAlreadyExist(String username);

    public ApplicationUser getUserById(Integer id);

    public List<ApplicationUser> getAllUsers();

    public void deleteUserById(Integer id);

    public Integer countUserByUsername(String username);
}
