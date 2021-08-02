package com.example.contributoryloanapp.utils;

import com.example.contributoryloanapp.model.ERole;
import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoleAssignment {

    public Set<Role> assignRole(Set<String> strRoles, RoleRepository roleRepository){

        Set<Role> roles = new HashSet<>();

        if(strRoles ==null){
            Role memberRole = roleRepository.findByName(ERole.BORROWER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found for the user"));

            System.out.println("MEMBER ROLE!!1"+ memberRole);

            roles.add(memberRole);
            System.out.println("THE ROLES STATE AFTER NULL: " + roles);
        }else{
            strRoles.forEach(role ->{
                switch (role){
                    case "ADMIN":
                        Role admin = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(admin);
                        System.out.println("THE ROLES STATE AFTER ADMIN CASE: " + roles);

                        break;
                    case "MEMBER":
                        Role member = roleRepository.findByName(ERole.MEMBER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(member);

                        System.out.println("THE ROLES STATE AFTER MEMBER CASE: " + roles);
                        break;
                    default:
                        Role borrower = roleRepository.findByName(ERole.BORROWER)
                                .orElseThrow(()-> new RuntimeException("Error: Role not found"));
                        roles.add(borrower);
                        System.out.println("THE ROLES STATE AFTER BORROWER : " + roles);
                }
            });
        }
        return  roles;
    }
}
