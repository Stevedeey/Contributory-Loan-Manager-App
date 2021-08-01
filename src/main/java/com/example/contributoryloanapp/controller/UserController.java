package com.example.contributoryloanapp.controller;

import com.example.contributoryloanapp.exception.ResourceNotFoundException;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.service.UserService;
import com.example.contributoryloanapp.service.serviceImplementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    private UserServiceImpl userService;

    @Autowired
   private UserRepository service;

    @GetMapping("/say_hello")
    public String sayHello(){
        return  "Hello World, I am liking it";
    }


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/find-user")
    public ResponseEntity<User> findUser(@PathVariable(value = "id") Long id){
        User user = service.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with the Id: "+id +" not found!!!"));
                return ResponseEntity.ok().body(user);

    }

    @DeleteMapping("/users/{id}")

    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id){
        User user = service.findById(id).get();
        service.delete(user);
        return new ResponseEntity(HttpStatus.OK);

    }

}
