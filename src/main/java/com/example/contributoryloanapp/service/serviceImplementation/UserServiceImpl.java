package com.example.contributoryloanapp.service.serviceImplementation;


import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.request.SignupRequest;
import com.example.contributoryloanapp.payload.response.MessageResponse;
import com.example.contributoryloanapp.repository.RoleRepository;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.service.UserService;
import com.example.contributoryloanapp.utils.RoleAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RoleAssignment roleAssignment;


    @Override
    public User saveUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new MessageResponse("Username is already taken");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new MessageResponse("Email is already exist");
        }

        System.out.println("I got into the saveUser RequestBody " + signupRequest);


        User user = new User(signupRequest.getUsername(), signupRequest.getFirstName(),
                signupRequest.getLastName(), signupRequest.getEmail(), signupRequest.getGender(),
                signupRequest.getDateOfBirth(), bCryptPasswordEncoder.encode(signupRequest.getPassword()));


        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = roleAssignment.assignRole(strRoles, roleRepository);

        user.setRoles(roles);

       userRepository.save(user);

        return user;
    }
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
//        if(userRepository.existsByUsername(signupRequest.getUsername())){
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Username is already taken"));
//        }
//
//        if(userRepository.existsByEmail(signupRequest.getEmail())){
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email specified is already in use"));
//        }
//
//        User user = new User(signupRequest.getUsername(),
//                signupRequest.getEmail(),
//                passwordEncoder.encode(signupRequest.getPassword()));
//
//
//        Set<String> strRoles = signupRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//
//        if(strRoles == null){
//            Role userRole = roleRepository.findByName(ERole.BORROWER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//            roles.add(userRole);
//        }else {
//            strRoles.forEach(role ->{
//                switch (role){
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//
//                        roles.add(adminRole);
//                        break;
//                    case "member":
//                        Role memberRole = roleRepository.findByName(ERole.MEMBER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//
//                        roles.add(memberRole);
//                        break;
//                    default:
//                        Role roleBorrower = roleRepository.findByName(ERole.BORROWER)
//                                .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
//
//                        roles.add(roleBorrower);
//                }
//            });
//        }
//        user.setRoles(roles);
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User Resgisterd succesfully"));
//    }


}
