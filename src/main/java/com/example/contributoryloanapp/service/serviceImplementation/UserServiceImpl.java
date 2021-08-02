package com.example.contributoryloanapp.service.serviceImplementation;


import com.example.contributoryloanapp.exception.ApiRequestException;
import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.request.SignupRequest;
import com.example.contributoryloanapp.payload.response.MessageResponse;
import com.example.contributoryloanapp.repository.RoleRepository;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.service.UserService;
import com.example.contributoryloanapp.utils.RoleAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Set;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RoleAssignment roleAssignment;


    @Override
    public User saveUser(SignupRequest signupRequest) {


        if (userRepository.existsByUsername(signupRequest.getUsername())) {
          throw  new ApiRequestException("Username is already taken");

        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
          throw new ApiRequestException("Email is already exist");

        }

        if (signupRequest.getEmail() == "") {
            throw  new ApiRequestException("Email cannot be empty!!!");

        }


        System.out.println("I got into the saveUser RequestBody " + signupRequest);


        User user = new User(signupRequest.getUsername(), signupRequest.getFirstName(),
                signupRequest.getLastName(), signupRequest.getEmail(), signupRequest.getGender(),
                signupRequest.getDateOfBirth(), bCryptPasswordEncoder.encode(signupRequest.getPassword()));


        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = roleAssignment.assignRole(strRoles, roleRepository);

        user.setRoles(roles);

        userRepository.save(user);

        return user;
    }

}
