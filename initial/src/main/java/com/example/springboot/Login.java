package com.example.springboot;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Login {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean userLogin(User user, List<String> usernames, List<String> passwords){
        for (int i=0;i<usernames.size();i++){
            if (usernames.get(i).equals(user.getUsername())){
                String encoded = passwords.get(i);
                boolean isPasswordMatch = passwordEncoder.matches(user.getPassword(), encoded);
                if (isPasswordMatch) {
                    return true;
                }
            }
        }
        return false;
    }

}
