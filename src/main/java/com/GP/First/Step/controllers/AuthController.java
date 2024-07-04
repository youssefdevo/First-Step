package com.GP.First.Step.controllers;


import com.GP.First.Step.DTO.request.LoginReq;
import com.GP.First.Step.DTO.response.ErrorRes;
import com.GP.First.Step.DTO.response.LoginRes;
import com.GP.First.Step.DTO.response.SuccessRes;
import com.GP.First.Step.auth.JwtUtil;
import com.GP.First.Step.entities.User;
import com.GP.First.Step.services.AuthenticationService;
import com.GP.First.Step.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for handling authentication-related requests.
 */
@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Constructor for injecting dependencies.
     *
     * @param authenticationService The service for handling authentication.
     * @param jwtUtil               The utility for handling JWT operations.
     * @param userService           The service for handling user operations.
     */
    @Autowired
    public AuthController(AuthenticationService authenticationService, JwtUtil jwtUtil, UserService userService) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Handles user login requests.
     *
     * @param loginReq The login request containing email and password.
     * @return A response entity with login success or error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        try {
            // Authenticate the user
            User user = authenticationService.authenticate(loginReq.getEmail(), loginReq.getPassword());

            // Create JWT token
            String token = jwtUtil.createToken(Optional.of(user));

            // Create response
            LoginRes loginRes = new LoginRes(user.getEmail(), token);
            return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Login successful", loginRes));
        } catch (BadCredentialsException e) {
            // Handle authentication failure
            ErrorRes errorResponse = new ErrorRes(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    /**
     * Handles user signup requests.
     *
     * @param newUser The new user to be created.
     * @return A response entity with signup success or error message.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User newUser) {
        // Check if the email is already in use
        if (userService.getUserByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorRes(HttpStatus.CONFLICT, "Email already in use"));
        }

        // Create and save the new user
        User savedUser = authenticationService.createUser(newUser);

        // Create JWT token
        String token = jwtUtil.createToken(Optional.of(savedUser));

        // Create response
        LoginRes loginRes = new LoginRes(savedUser.getEmail(), token);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessRes(HttpStatus.OK, "Signup successful", loginRes));

    }

    /**
     * Handles user logout requests.
     *
     * @return A response entity indicating logout success.
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        // Clear the security context
        SecurityContextHolder.clearContext();

        // Return logout success response
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Logout successful", null));
    }
}