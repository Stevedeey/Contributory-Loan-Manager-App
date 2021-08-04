package com.example.contributoryloanapp.payload.request;

import lombok.Data;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.*;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotNull(message = "first name cannot be empty")
    private String firstName;

    @NotNull(message = "last name cannot be empty")
    private String lastName;

    @NotBlank(message = "email cannot be empty")
    @Size(max = 50)
    @Email(message = "must be email")
    private String email;

    private String gender;

    private String dateOfBirth;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotNull(message = "password cannot be empty")
    @Size(min = 6, max = 24)
    private String confirmPassword;

    private Set<String> role;



}