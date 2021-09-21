package com.example.springboot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table
public class User
{

    @Id
    @Column
    @Size(min=1,message="Username cannot be null")
    private String username;

    @Size(min=1,message="Password cannot be null")
    private String password;

    public User(){}

    public User(String username, String password){
        this.username=username;
        this.password=scramblePassword(password);
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username= username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = scramblePassword(password);
    }

    public String scramblePassword(String password){
        String newPassword = "";
        for (int i = password.length() - 1; i >= 0; i--) {
            newPassword = newPassword + password.charAt(i);
        }
        return newPassword;
    }
}