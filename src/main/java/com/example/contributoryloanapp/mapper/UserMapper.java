package com.example.contributoryloanapp.mapper;

import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMapper {

    private String firstName;

    private String lastName;

    private  String email;

    private String gender;

    private String dateOfBirth;

   private Set<Role> roles;


}
