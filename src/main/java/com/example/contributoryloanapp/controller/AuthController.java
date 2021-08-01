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
    public UserResponseDTO registerUser(@Valid @RequestBody SignupRequest signupRequest) throws UnirestException {
        User user = userService.saveUser(signupRequest);
        return UserResponseDTO.build(user);
    }

//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
//    if(userRepository.existsByUsername(signupRequest.getUsername())){
//        return ResponseEntity
//                .badRequest()
//                .body(new MessageResponse("Username is already taken"));
//    }
//
//    if(userRepository.existsByEmail(signupRequest.getEmail())){
//        return ResponseEntity
//                .badRequest()
//                .body(new MessageResponse("Error: Email specified is already in use"));
//    }
//
//
//
//        User user = new User(signupRequest.getUsername(), signupRequest.getFirstName(),
//                signupRequest.getLastName(), signupRequest.getEmail(), signupRequest.getGender(),
//                signupRequest.getDateOfBirth(), bCryptPasswordEncoder.encode(signupRequest.getPassword()));
//
//
//
//        Set<String> strRoles = signupRequest.getRole();
//    Set<Role> roles = new HashSet<>();
//
//    if(strRoles == null){
//        Role userRole = roleRepository.findByName(ERole.BORROWER)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//        roles.add(userRole);
//    }else {
//        strRoles.forEach(role ->{
//            switch (role){
//                case "ADMIN":
//                    Role adminRole = roleRepository.findByName(ERole.ADMIN)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//
//                    roles.add(adminRole);
//                    break;
//                case "MEMBER":
//                    Role memberRole = roleRepository.findByName(ERole.MEMBER)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//
//                    roles.add(memberRole);
//                    break;
//                default:
//                    Role roleBorrower = roleRepository.findByName(ERole.BORROWER)
//                            .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
//
//                    roles.add(roleBorrower);
//            }
//        });
//    }
//    user.setRoles(roles);
//    userRepository.save(user);
//
//    return ResponseEntity.ok(new MessageResponse("User Resgisterd succesfully"));
//}
}
