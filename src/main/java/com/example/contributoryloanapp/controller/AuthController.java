package com.example.contributoryloanapp.controller;

import com.example.contributoryloanapp.model.ERole;
import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.request.SignupRequest;
import com.example.contributoryloanapp.payload.response.MessageResponse;
import com.example.contributoryloanapp.payload.response.UserResponseDTO;
import com.example.contributoryloanapp.repository.RoleRepository;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.security.jwt.JwtUtils;
import com.example.contributoryloanapp.service.UserService;
import com.example.contributoryloanapp.utils.RoleAssignment;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;



    @PostMapping("/signup")
    public UserResponseDTO registerUser(@Valid @RequestBody SignupRequest signupRequest){

        return UserResponseDTO.build(userService.saveUser(signupRequest));

    }

}
