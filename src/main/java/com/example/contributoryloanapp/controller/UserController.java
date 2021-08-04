package com.example.contributoryloanapp.controller;

import com.example.contributoryloanapp.dto.UserDTO;
import com.example.contributoryloanapp.exception.ApiRequestException;
import com.example.contributoryloanapp.mapper.UserMapper;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.response.auth.EditUser;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.service.UserService;
import com.example.contributoryloanapp.service.serviceImplementation.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {



    private ModelMapper modelMapper;

    @Autowired
   private UserService userService;


     @PutMapping("/edit-user")
     @Secured({"ADMIN", "MEMBER", "BORROWER"})
     public ResponseEntity<?> editUser(@Valid @RequestBody EditUser editUser){

       var user  = userService.updateUser(editUser);

         return new ResponseEntity<>(user, HttpStatus.OK);
     }


    @GetMapping("/user-details")
    @Secured({"ADMIN", "MEMBER", "BORROWER"})
    public ResponseEntity<?>  getUserDetails(){
        return new ResponseEntity<>(userService.getUserDetails(), HttpStatus.OK);
    }


    @GetMapping("/get-all-users")
    public ResponseEntity<Set<UserMapper>> getAllUsers(){

        Set<UserMapper> dtoUsersList = userService.getAllUsers();

        return ResponseEntity.ok(dtoUsersList);
    }

    @GetMapping("/get-users-by-role/{role}")
    public ResponseEntity<Set<UserMapper>> getUsersByRole(@PathVariable String role){

        Set<UserMapper> dtoUsersList = userService.getUsersByRole(role);

        return ResponseEntity.ok(dtoUsersList);
    }



}
