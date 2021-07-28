package com.example.contributoryloanapp.controller;

import com.example.contributoryloanapp.exception.ResourceNotFoundException;
import com.example.contributoryloanapp.model.User;
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
    private UserServiceImpl userServiceImpl;

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/say_hello")
    public String sayHello(){
        return  "Hello World, I am liking it";
    }

    @PostMapping("/employee")
    public User saveUser(@RequestBody User user){
        User person =  userServiceImpl.createUser(user);

        return  userService.save(person);

    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/find-user")
    public ResponseEntity<User> findUser(@PathVariable(value = "id") Long id){
        User user = userService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with the Id: "+id +" not found!!!"));
                return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(value = "id") Long id){
        User user = userService.findById(id).get();
        userService.delete(user);
    }

}
