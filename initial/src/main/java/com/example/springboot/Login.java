package com.example.springboot;
import org.springframework.stereotype.Component;

@Component
public class Login {

    private String[] usernameList= {"Jessica", "James", "Jacob"};
    private String[] passwordList= {"Jessica", "James", "Jacob"};

    public String userLogin(String username, String password ){
        for (int i=0;i<usernameList.length;i++){
            if (usernameList[i].equals(username)){
                if (passwordList[i].equals(password)){
                    return username.concat(password);
                }
            }
        }
        return "Invalid";
    }
}
