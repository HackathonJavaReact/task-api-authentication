package com.api.authentication.authenticationapi.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "application_users")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Transient
    private String confirmPassword;

    @Email(message = "The email is not valid")
    private String email;

    @Column(name = "is_account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "is_account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "is_credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Transient
    private Set<? extends GrantedAuthority> grantedAuthorities;

    public ApplicationUser() {

    }

    @Autowired
    public ApplicationUser(
            String username,
            String password,
            String confirmPassword,
            String email,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            boolean enabled,
            Set<? extends GrantedAuthority> grantedAuthorities
    ) {

        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean isAccountNonExpired) {
        this.accountNonExpired = isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean isAccountNonLocked) {
        this.accountNonLocked = isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
        this.credentialsNonExpired = isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return enabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<? extends GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

}
