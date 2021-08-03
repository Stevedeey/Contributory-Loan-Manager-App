package com.example.contributoryloanapp.service;


import com.example.contributoryloanapp.dto.UserDTO;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.request.SignupRequest;
import com.example.contributoryloanapp.payload.response.auth.ForgotPasswordResponse;
import com.example.contributoryloanapp.payload.response.auth.EditUser;
import com.example.contributoryloanapp.payload.response.auth.UpdatePasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


public interface UserService {

    //This to be edited
    User saveUser(SignupRequest signupRequest);
    User findUserByEmail(String email);
    Optional<User> findUserByResetToken(String resetToken);
    Optional<User> getUserByEmail(String email);
    void deactivateResetPasswordToken();
    ResponseEntity<ForgotPasswordResponse> userForgotPassword(HttpServletRequest request, String accountEmail);
    UserDTO updateUser(EditUser user);
    boolean changeUserPassword(User user,  UpdatePasswordRequest updatePasswordRequest);
    User findUserById(Long userId);
    UserDTO getUserDetails();

    }


