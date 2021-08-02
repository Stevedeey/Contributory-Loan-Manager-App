package com.example.contributoryloanapp.model;



import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
//@Table(name = "users",
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "username"),
//                @UniqueConstraint(columnNames = "email")
//        })

@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel{


    @Size(max = 20)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(max = 50)
    @Email
    private  String email;

    private String gender;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "password_reset_token", columnDefinition = "TEXT")
    private String passwordResetToken;

    @Column(name = "password_reset_expire_date")
    private String passwordResetExpireDate;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String firstName, String lastName, String email,
                String gender, String dateOfBirth, String password) {

        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.password = password;

    }

    public User(String username, String firstName, String lastName,
                String email, String gender, String dateOfBirth,
                String password, Set<Role> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.roles = roles;
    }
}
