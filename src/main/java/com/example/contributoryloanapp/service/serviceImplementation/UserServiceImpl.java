package com.example.contributoryloanapp.service.serviceImplementation;


import com.example.contributoryloanapp.dto.UserDTO;
import com.example.contributoryloanapp.exception.ApiRequestException;
import com.example.contributoryloanapp.exception.ResourceNotFoundException;
import com.example.contributoryloanapp.mapper.UserMapper;
import com.example.contributoryloanapp.model.ERole;
import com.example.contributoryloanapp.model.Role;
import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.payload.request.LoginRequest;
import com.example.contributoryloanapp.payload.request.SignupRequest;
import com.example.contributoryloanapp.payload.response.JwtResponse;
import com.example.contributoryloanapp.payload.response.auth.ForgotPasswordResponse;
import com.example.contributoryloanapp.payload.response.auth.EditUser;
import com.example.contributoryloanapp.payload.response.auth.ResetPassword;
import com.example.contributoryloanapp.payload.response.auth.UpdatePasswordRequest;
import com.example.contributoryloanapp.repository.RoleRepository;
import com.example.contributoryloanapp.repository.UserRepository;
import com.example.contributoryloanapp.security.jwt.JwtUtils;
import com.example.contributoryloanapp.security.services.UserDetailsImpl;
import com.example.contributoryloanapp.service.UserService;
import com.example.contributoryloanapp.utils.DateUtils;
import com.example.contributoryloanapp.utils.classes.RoleAssignment;
import com.example.contributoryloanapp.utils.mailService.MailService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private RoleAssignment roleAssignment;

    @Autowired
    private MailService mailService;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;


    @Override
    public User saveUser(SignupRequest signupRequest) {


        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ApiRequestException("Username is already taken");

        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ApiRequestException("Email is already exist");

        }

        if(!isEmailValid(signupRequest.getEmail())){
            throw new ApiRequestException("Invalid Email Format!");

        }

        if(!isValidPassword(signupRequest.getPassword())){
            throw new ApiRequestException("Invalid password!");
        }

        if(!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())){

            throw new ApiRequestException("Password mismatched!");
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getFirstName(),
                signupRequest.getLastName(), signupRequest.getEmail(), signupRequest.getGender(),
                signupRequest.getDateOfBirth(), bCryptPasswordEncoder.encode(signupRequest.getPassword()));


        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = roleAssignment.assignRole(strRoles, roleRepository);

        user.setRoles(roles);

        userRepository.save(user);

        return user;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest){

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()));

        } catch (BadCredentialsException e) {

            throw new ApiRequestException("Incorrect Username or Password");

        }

        //context holder holding the authentication object for future validation and authorization
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // UserDetails userDetails1 =  userDetailsService.loadUserByUsername(loginRequest.getUsername());
        //alternative to the line above to retrieve userdetails

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());

        return  new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);

    }

    public ResponseEntity<String> updatePassword(UpdatePasswordRequest updatePasswordRequest){

        User user = getLoggedInUser();

        checkIfValidOldPassword(user, updatePasswordRequest);

        boolean  isPassWordChanged = changeUserPassword( user,   updatePasswordRequest);

        if(isPassWordChanged){

            return ResponseEntity.ok("Password Changed Successfully");
        }
        return new ResponseEntity<>("Operation failed!!", HttpStatus.BAD_REQUEST);
    }


    private boolean isValidPassword(String password) {
        String regex = "^(([0-9]|[a-z]|[A-Z]|[@])*){8,20}$";

        Pattern pattern = Pattern.compile(regex);
        if (password == null) {
            throw new ApiRequestException("Error: password cannot ne null");
        }

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isEmailValid(String password) {
        String regex = "^(.+)@(\\w+)\\.(\\w+)$";

        Pattern pattern = Pattern.compile(regex);
        if (password == null) {
            throw new ApiRequestException("Error: email cannot ne null");
        }

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public Optional<User> findUserByResetToken(String resetToken) {
        return userRepository.findByPasswordResetToken(resetToken);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public void deactivateResetPasswordToken() {
        List<User> accountsList = userRepository.findAllByPasswordResetTokenIsNotNull();
        accountsList.forEach(account -> {
            String expireDate = account.getPasswordResetExpireDate();
            String presentDate = DateUtils.getCurrentTime();
            int actionDelete = presentDate.compareTo(expireDate);
            if (actionDelete > 0 || actionDelete == 0) {
                account.setPasswordResetExpireDate(null);
                account.setPasswordResetToken(null);
                userRepository.save(account);
            }
        });
    }

    @Override
    public ResponseEntity<ForgotPasswordResponse> userForgotPassword(HttpServletRequest request, String username) {
        // Lookup user in database by e-mail
        //Optional<User> optionalUser = userRepository.findByEmail(accountEmail);

        // This allow to ensure the variables don't give null related errors when invoked
        var ref = new Object() {
            Role roleMember = null;
            Role roleBorrower = null;
        };

        Optional<User> user = userRepository.findByUsername(username);
        //response handler
        ForgotPasswordResponse responseHandler = new ForgotPasswordResponse();

        Set<Role> roles = user.get().getRoles();
        var role = user.get().getRoles().iterator().next().getName().toString();

        System.out.println("ROLES!!!! " + roles);
        System.out.println("ROLE!!!! " + role);


        roles.forEach(eachRole -> {
            switch (role) {
                case "MEMBER":
                    ref.roleMember = roleRepository.findByName(ERole.MEMBER)
                            .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                    break;

                case "BORROWER":
                    System.out.println("I got into the Borrower case block");
                    ref.roleBorrower = roleRepository.findByName(ERole.BORROWER)
                            .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));

                    System.out.println("Then I got: " + ref.roleBorrower);

                    break;
            }
        });


        if (!user.isPresent()) {
            responseHandler.setStatus(404);
            responseHandler.setMessage("We couldn't find an account with that e-mail address.");
            return new ResponseEntity<>(responseHandler, HttpStatus.NOT_FOUND);
        }

        Role userRole = user.get().getRoles().iterator().next();

        System.out.println("UserRole: " + userRole);
        System.out.println("RoleBorrower: " + ref.roleBorrower);
        System.out.println("RoleMember: " + ref.roleMember);
        System.out.println((userRole != ref.roleBorrower) + "TRUE/FALSE1");
        System.out.println((userRole != ref.roleMember) + "TRUE/FALSE2");

        if (userRole == ref.roleBorrower || userRole == ref.roleMember) {

            try {
                // Generate random 36-character string token for reset password
//            User userN = user.get();
//            userN.setPasswordResetToken(UUID.randomUUID().toString());
//            //24hours expiry date for token
//            String tokenExpiryDate = DateUtils.passwordResetExpiryTimeLimit();
//            userN.setPasswordResetExpireDate(tokenExpiryDate);
//
//            String appUrl = request.getScheme() + "://" + request.getServerName();
//            String subject = "Customer Reset Password";
//            String mailBody = "To reset your password, click the link below:\n"
//                    + appUrl + "/reset?token="
//                    + userN.getPasswordResetToken();
//            mailService.sendMessage(userN.getEmail(), subject, mailBody);
//            // Save token and expiring date to database
//            userRepository.save(userN);
//            responseHandler.setStatus(200);
                responseHandler.setMessage("Successfully sent email");
            }
//        catch (UnirestException e){
            catch (Exception e) {

                System.out.println("Error sending email:\n\tError message:" + e.getMessage());
            }

        } else {
            responseHandler.setStatus(401);
            responseHandler.setMessage("You don't have access to this link");
            return new ResponseEntity<>(responseHandler, HttpStatus.UNAUTHORIZED);
        }
        //process email

        return new ResponseEntity<>(responseHandler, HttpStatus.OK);
    }


    public ResponseEntity<ForgotPasswordResponse> userResetPassword(ResetPassword passwordReset) {

        //find the user by the token
        Optional<User> userOptional = userRepository.findByPasswordResetToken(passwordReset.getToken());

        String password = passwordReset.getPassword();
        String confirmPassword = passwordReset.getConfirmPassword();

        //response handler
        ForgotPasswordResponse responseHandler = new ForgotPasswordResponse();

        if (userOptional.isEmpty()) {
            responseHandler.setStatus(400);
            responseHandler.setMessage("Oops!  This is an invalid password reset link.");
            return new ResponseEntity<>(responseHandler, HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        if (!password.equals(confirmPassword)) {
            responseHandler.setStatus(400);
            responseHandler.setMessage("Passwords does not match");
            return new ResponseEntity<>(responseHandler, HttpStatus.BAD_REQUEST);
        }

        //set the encrypted password
        user.setPassword(bCryptPasswordEncoder.encode(password));
        // Set the reset token to null so it cannot be used again
        user.setPasswordResetToken(null);
        //set the reset passwordRestExpireDate to null
        user.setPasswordResetExpireDate(null);
        try {
            // Save user
            userRepository.save(user);
            responseHandler.setStatus(201);
            responseHandler.setMessage("You have successfully reset your password. You can now login.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(responseHandler, HttpStatus.CREATED);
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            throw new ResourceNotFoundException("Incorrect parameter; email " + email + " does not exist");
        return user.get();
    }

    @Override
    public UserDTO updateUser(EditUser user) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = getLoggedInUser();


        loggedUser.setUsername(user.getUserName());
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setEmail(user.getEmail());
        loggedUser.setLastName(user.getLastName());
        loggedUser.setGender(user.getGender());
        loggedUser.setDateOfBirth(user.getDateOfBirth());

        if (!userRepository.existsByUsername(user.getUserName())) {
            loggedUser.setUsername(user.getUserName());
        } else if (user.getUserName().equals(loggedUser.getUsername())) {
            loggedUser.setUsername(user.getUserName());
        } else if (user.getUserName() == null || user.getUserName().equals("")) {
            loggedUser.setUsername(username);
        } else throw new ApiRequestException("Error: User with this username already exist");

        loggedUser = userRepository.save(loggedUser);

        return new UserDTO("Profile successfully updated",
                loggedUser.getUsername(),
                loggedUser.getFirstName(),
                loggedUser.getLastName(),
                loggedUser.getEmail(),
                loggedUser.getGender(),
                loggedUser.getDateOfBirth(),
                loggedUser.getRoles());
    }

    public boolean checkIfValidOldPassword(User user, UpdatePasswordRequest updatePasswordRequest) {

        String newPassword = updatePasswordRequest.getNewPassword();
        String confirmNewPassword = updatePasswordRequest.getConfirmNewPassword();

        boolean passwordMatch = newPassword.equals(confirmNewPassword);

        boolean matches = bCryptPasswordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword());

        if (!passwordMatch) {
            throw new ApiRequestException("Passwords do not match");
        }

        if (!matches) {
            throw new ApiRequestException("Old password supplied is wrong");
        }

        return true;
    }

    @Override
    public boolean changeUserPassword(User user, UpdatePasswordRequest updatePasswordRequest) {

        String newPassword = updatePasswordRequest.getNewPassword();
        String confirmNewPassword = updatePasswordRequest.getConfirmNewPassword();

        if (newPassword.equals(confirmNewPassword)) {
            user.setPassword(bCryptPasswordEncoder.encode(updatePasswordRequest.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User findUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new ResourceNotFoundException("Incorrect parameter; email " + userId + " does not exist");
        return user.get();
    }

    @Override
    public UserDTO getUserDetails() {
        User user = getLoggedInUser();
        return UserDTO.build(user);
    }

    public User getLoggedInUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(
                () -> new ApiRequestException("User not logged in")
        );
    }

    public Set<UserMapper> getAllUsers() {
        var users = userRepository.findAll();

        //mapping to ensure that unnecessary members of the user and userDTO are not revealed when viewing all users
        Set<UserMapper> dtoUserList = users.stream().map(user -> {
            UserMapper userMapper = modelMapper.map(user, UserMapper.class);
            return userMapper;
        }).collect(Collectors.toSet());

        return dtoUserList;
    }

    public Set<UserMapper> getUsersByRole(String role) {

        Set<UserMapper> userList = getAllUsers();
        Set<UserMapper> userMapperList = new HashSet<>();

        switch (role.toUpperCase()) {
            case "ADMIN":

                userMapperList = userList.stream().filter(each -> each.getRoles()
                        .iterator()
                        .next()
                        .getName()
                        .equals(ERole.ADMIN))
                        .collect(Collectors.toSet());

                break;

            case "BORROWER":

                userMapperList = userList.stream().filter(each -> each.getRoles()
                        .iterator()
                        .next()
                        .getName()
                        .equals(ERole.valueOf("BORROWER")))
                        .collect(Collectors.toSet());

                break;

            case "MEMBER":

                userMapperList = userList.stream().filter(each -> each.getRoles()
                        .iterator()
                        .next()
                        .getName()
                        .equals(ERole.valueOf("MEMBER")))
                        .collect(Collectors.toSet());

                break;
            default:
                break;
        }
        return userMapperList;
    }

}
