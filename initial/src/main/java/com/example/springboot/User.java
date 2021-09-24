package com.example.springboot;

//h2 database tab;e
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//Validation
import javax.validation.constraints.Size;

//Password Encryption
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;


@Entity
@Table
public class User {

    //Attributes
    @Id
    @Column
    @Size(min=1,message="Username cannot be null")
    private String username;

    @Size(min=1,message="Password cannot be null")
    private String password;

    @Size(min=1,message="Token cannot be null")
    private String token;

    //Constructors
    public User(){}

    public User(String username, String password){
        this.username=username;
        this.password=encryptPassword(password);
    }

    //Methods
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

    public void setPassword(String password){
        this.password = encryptPassword(password);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //Password Encryption
    public String encryptPassword(String password) {
        String newPassword = "";
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            newPassword = s.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return newPassword;
    }
}