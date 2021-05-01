package com.api.authentication.authenticationapi.service.dao;

import java.util.List;

import com.api.authentication.authenticationapi.model.ApplicationUser;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String username);

    public boolean addNewUser(ApplicationUser user);

    public boolean emailAlreadyExist(String email);

    public boolean usernameAlreadyExist(String username);

    public Integer countUserByUsername(String username);
}
