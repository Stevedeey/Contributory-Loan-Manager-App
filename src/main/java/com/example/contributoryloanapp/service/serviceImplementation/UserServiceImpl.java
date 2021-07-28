package com.example.contributoryloanapp.service.serviceImplementation;

import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){

        User person = new User();

        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());
        person.setEmail(user.getEmail());
        person.setFirstName(user.getPassword());
        person.setDateOfBirth(user.getDateOfBirth());
        person.setGender(user.getGender());


        userRepository.save(person);

        return person;
    }

}
