package com.example.contributoryloanapp.service;


import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.request.SignupRequest;


public interface UserService {

    User saveUser(SignupRequest signupRequest);
}
