package com.example.contributoryloanapp.controller;

import com.example.contributoryloanapp.exception.ApiRequestException;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.request.LoginRequest;
import com.example.contributoryloanapp.payload.request.SignupRequest;
import com.example.contributoryloanapp.payload.response.JwtResponse;
import com.example.contributoryloanapp.payload.response.UserResponseDTO;
import com.example.contributoryloanapp.payload.response.auth.UpdatePasswordRequest;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.security.jwt.JwtUtils;
import com.example.contributoryloanapp.security.services.UserDetailsImpl;
import com.example.contributoryloanapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


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
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsService userDetailsService;



    @PostMapping("/signup")
    public UserResponseDTO registerUser(@Valid @RequestBody SignupRequest signupRequest){

        return UserResponseDTO.build(userService.saveUser(signupRequest));

    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception{
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                             loginRequest.getPassword()));

        } catch (BadCredentialsException e) {

          throw new ApiRequestException("Incorrect Username or Password");

        }

        //context holder holding the authentication object for future validation and authorization
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

       // UserDetails userDetails1 =  userDetailsService.loadUserByUsername(loginRequest.getUsername());
        //alternative to the line above to retrieve userdetails

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());

        return  ResponseEntity.ok(new JwtResponse(jwt,
                                                    userDetails.getId(),
                                                    userDetails.getUsername(),
                                                    userDetails.getEmail(),
                                                    roles));
    }


    @PostMapping("/change-password")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER', 'BORROWER')")
////    @Secured({"ADMIN","MEMBER","BORROWER"})
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest){

        User user = userService.getLoggedInUser();

        userService.checkIfValidOldPassword(user, updatePasswordRequest);

        boolean  isPassWordChanged = userService.changeUserPassword( user,   updatePasswordRequest);

        if(isPassWordChanged){

            return ResponseEntity.ok("Password Changed Successfully");
        }
        return new ResponseEntity<>("Operation failed!!", HttpStatus.BAD_REQUEST);

    }
}
