package com.example.contributoryloanapp.payload.response;

import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
public class UserResponseDTO {

    private String message;

    private String firstName;

    private String lastName;

    private  String email;

    private String gender;

    private String dateOfBirth;

   private Set<Role> roles;


    public static UserResponseDTO build(User user){
        return new UserResponseDTO(
                "Registration Successful",
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender(),
                user.getDateOfBirth(),
                user.getRoles()
        );
    }
}
