package com.example.springboot;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Login {

    private String[] usernameList= {"Jessica", "James", "Jacob"};
    private String[] passwordList= {"Jessica", "James", "Jacob"};

    public boolean userLogin(String username, String password ){
        for (int i=0;i<usernameList.length;i++){
            if (usernameList[i].equals(username)){
                if (passwordList[i].equals(password)){
                    return true;
                }
            }
        }
        return false;
    }
}
