package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.springboot.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public void add(User user)
    {
        userRepository.save(user);
    }
}
