package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.springboot.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public void add(User user){
        userRepository.save(user);
    }

    public List<String> getAllUsernames(){
        List<String> usernames = new ArrayList<String>();
        userRepository.findAll().forEach(user -> usernames.add(user.getUsername()));
        return usernames;
    }

    public List<String> getAllPasswords(){
        List<String> users=new ArrayList<String>();
        userRepository.findAll().forEach(user -> users.add(user.getPassword()));
        return users;
    }

}
