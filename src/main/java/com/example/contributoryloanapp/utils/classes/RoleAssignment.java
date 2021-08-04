package com.example.contributoryloanapp.utils.classes;

import com.example.contributoryloanapp.exception.ApiRequestException;
import com.example.contributoryloanapp.model.ERole;
import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RoleAssignment {

//    public Set<Role> assignRole(Set<String> strRoles, RoleRepository roleRepository){
    public Set<Role> assignRole(Set<String> strRoles, RoleRepository roleRepository){

      Set<Role> roles = new HashSet<>();

        if(strRoles ==null){
            Role memberRole = roleRepository.findByName(ERole.BORROWER)
                    .orElseThrow(() -> new ApiRequestException("Error: Role not found for the user"));

            System.out.println("MEMBER ROLE!!1"+ memberRole);

            roles.add(memberRole);
            System.out.println("THE ROLES STATE AFTER NULL: " + roles);
        }else{
            strRoles.forEach(role ->{
                switch (role){
                    case "ADMIN":
                        Role admin = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new ApiRequestException("Error: Role not found"));
                        roles.add(admin);

                        break;

                    case "MEMBER":
                        Role member = roleRepository.findByName(ERole.MEMBER)
                                .orElseThrow(() -> new ApiRequestException("Error: Role not found"));
                        roles.add(member);

                        break;

                    default:
                        Role borrower = roleRepository.findByName(ERole.BORROWER)
                                .orElseThrow(()-> new ApiRequestException("Error: Role not found"));
                        roles.add(borrower);

                        break;
                }
            });
        }
        return  roles;
    }
}
