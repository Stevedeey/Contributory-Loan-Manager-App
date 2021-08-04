package com.example.contributoryloanapp.controller;

import com.example.contributoryloanapp.payload.response.auth.ForgotPasswordResponse;
import com.example.contributoryloanapp.payload.response.auth.ResetPassword;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.service.serviceImplementation.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    @Autowired
    private AdminService adminService;



//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/password-forgot")
    @Secured("ADMIN")
    public ResponseEntity<ForgotPasswordResponse> adminForgotPassword(@RequestParam("username") String username, HttpServletRequest request){
        return adminService.adminForgotPassword(request, username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/password-reset")
    @Secured("ADMIN")
    public ResponseEntity<ForgotPasswordResponse> adminResetPassword(@Valid @RequestBody ResetPassword resetPassword) {
        return adminService.adminResetPassword(resetPassword);
    }
}
