package com.example.contributoryloanapp.service.serviceImplementation;

import com.example.contributoryloanapp.payload.request.LoginRequest;
import com.example.contributoryloanapp.payload.response.JwtResponse;
import com.example.contributoryloanapp.payload.response.auth.ForgotPasswordResponse;
import com.example.contributoryloanapp.payload.response.auth.ResetPassword;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AdminService {
    ResponseEntity<ForgotPasswordResponse> adminForgotPassword(HttpServletRequest req, String email);
    ResponseEntity<ForgotPasswordResponse> adminResetPassword(ResetPassword resetPassword);
}
