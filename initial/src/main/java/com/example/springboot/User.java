package com.example.springboot;

import javax.validation.constraints.Size;

public class User {
    @Size(min=1,message="Username cannot be null")
    private String username;
    @Size(min=1,message="Password cannot be null")
    private String password;

    public User(String username, String password ) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
