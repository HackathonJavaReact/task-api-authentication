package com.api.authentication.authenticationapi.dto;

public class ApplicationUserDto {
    private int id;
    private String username;
    private String email;

    public ApplicationUserDto(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public ApplicationUserDto(){

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    
}
