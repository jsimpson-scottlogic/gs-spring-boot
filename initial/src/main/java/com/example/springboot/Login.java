package com.example.springboot;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Login {

    @Autowired
    UserService userService;

    public boolean userLogin(String username, String password, List<String> usernames, List<String> passwords){
        for (int i=0;i<usernames.size();i++){
            if (usernames.get(i).equals(username)){
                if (passwords.get(i).equals(password)){
                    return true;
                }
            }

        }
        return false;
    }

}
