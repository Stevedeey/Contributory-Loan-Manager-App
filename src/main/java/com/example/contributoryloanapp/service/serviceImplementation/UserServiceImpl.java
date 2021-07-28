package com.example.contributoryloanapp.service.serviceImplementation;

import com.example.contributoryloanapp.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    public User createUser(User user){
        user.setFirstName("Oluwatosin");
        user.setLastName("Olaleye");
        user.setEmail("stvoluto69@gmail.com");
        user.setPassword("1234567890");
        user.setDateOfBirth("343");
        user.setGender("Male");

        User person = new User(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getDateOfBirth(),
                user.getGender());

        return person;
    }

}
