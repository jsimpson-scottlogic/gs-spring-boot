package com.example.springboot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Login {

    @Autowired
    User user;

    public String userLogin(String username, String password, List<String> usernames, List<String> passwords){
        for (int i=0;i<usernames.size();i++){
            if (usernames.get(i).equals(username)){
                if (passwords.get(i).equals(user.scramblePassword(password))){
                    return username.concat(password);
                }
            }

        }
        return "Invalid";
    }
}
