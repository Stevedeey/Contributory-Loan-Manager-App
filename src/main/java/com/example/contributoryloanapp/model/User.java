package com.example.contributoryloanapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel{

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private  String email;

    private String gender;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    private String password;

    @Column(name = "password_reset_token", columnDefinition = "TEXT")
    private String passwordResetToken;

    @Column(name = "password_reset_expire_date")
    private String passwordResetExpireDate;

    public User(String firstName, String lastName, String email, String gender, String dateOfBirth, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }

}
