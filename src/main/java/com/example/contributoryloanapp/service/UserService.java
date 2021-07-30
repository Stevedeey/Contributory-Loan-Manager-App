package com.example.contributoryloanapp.service;


import com.example.contributoryloanapp.model.User;


public interface UserService {
    public User createUser(User user);
    User saveUser(User user);
}
