package com.example.contributoryloanapp.service.serviceImplementation;

import com.example.contributoryloanapp.exception.ResourceNotFoundException;
import com.example.contributoryloanapp.model.ERole;
import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.response.auth.ForgotPasswordResponse;
import com.example.contributoryloanapp.payload.response.auth.ResetPassword;
import com.example.contributoryloanapp.repository.RoleRepository;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.service.UserService;
import com.example.contributoryloanapp.utils.DateUtils;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public ResponseEntity<ForgotPasswordResponse> adminForgotPassword(HttpServletRequest req, String username) {

        // Lookup user in database by e-mail
        Optional<User> adminOptional = userRepository.findByUsername(username);

        System.out.println("Admin optional"+adminOptional);

        //response handler
        ForgotPasswordResponse res = new ForgotPasswordResponse();


        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));

        System.out.println("Admin Role "+ adminRole);

        if(adminOptional.isEmpty()) {
            res.setStatus(404);
            res.setMessage("We couldn't find an account with that username");

            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }

        Role role = adminOptional.get().getRoles().iterator().next();

        if(role != adminRole){
            res.setStatus(401);
            res.setMessage("You are not authorized to visit this link");

            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }

        try{
            // Generate random 36-character string token for reset password
//            User admin = adminOptional.get();
//
//            admin.setPasswordResetToken(UUID.randomUUID().toString());
//
//            //24hours expiry date for token
//            String tokenExpiryDate = DateUtils.passwordResetExpiryTimeLimit();
//
//            admin.setPasswordResetExpireDate(tokenExpiryDate);
//
//            String appUrl = req.getScheme() + "://" + req.getServerName();
//
//            String subject = "Admin Reset Password";
//
//            String mailBody = "To reset your password, click the link below:\n"
//                    + appUrl + "/reset?token="
//                    + admin.getPasswordResetToken();
//
//            mailService.sendMessage(admin.getEmail(), subject, mailBody);
//
//            // Save token and expiring date to database
//            userService.saveUser(admin);
//
//            res.setStatus(200);
            res.setMessage("Successfully sent email");

//        }catch (UnirestException e){
        }catch (Exception e){
            System.out.println("Error sending email:\n\tError message:"+e.getMessage());
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ForgotPasswordResponse> adminResetPassword(ResetPassword resetPassword) {
        //find the admin by the token
        Optional<User> adminOptional = userService.findUserByResetToken(resetPassword.getToken());

        String password = resetPassword.getPassword();
        String confirmPassword = resetPassword.getConfirmPassword();

        //response handler
        ForgotPasswordResponse res = new ForgotPasswordResponse();

        if (adminOptional.isEmpty()){
            res.setStatus(400);
            res.setMessage("Oops!  This is an invalid password reset link.");

            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        User admin = adminOptional.get();

        if(!password.equals(confirmPassword)){
            res.setStatus(400);
            res.setMessage("Passwords does not match");

            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        //set the encrypted password
        admin.setPassword(bCryptPasswordEncoder.encode(password));

        // Set the reset token to null, so it cannot be used again
        admin.setPasswordResetToken(null);

        //set the reset passwordRestExpireDate to null
        admin.setPasswordResetExpireDate(null);


        try {
            // Save person
            //userService.saveUser(admin);

            userRepository.save(admin);

            res.setStatus(201);
            res.setMessage("You have successfully reset your password. You can now login.");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }
}
