package com.example.springboot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

//mark class as an Entity
@Entity
//defining class name as Table name
@Table
public class User
{
    @Id
    @Column
    private int id;

    @Column
    @Size(min=1,message="Username cannot be null")
    private String username;

    @Column
    @Size(min=1,message="Password cannot be null")
    private String password;

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
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
        this.password = password;
    }

    public User(int id, String username, String password){
        this.id=id;
        this.username=username;
        this.password=password;
    }

    public User(){}
}