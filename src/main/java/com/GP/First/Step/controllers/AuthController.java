package com.GP.First.Step.controllers;


import com.GP.First.Step.DTO.request.LoginReq;
import com.GP.First.Step.DTO.response.ErrorRes;
import com.GP.First.Step.DTO.response.LoginRes;
import com.GP.First.Step.DTO.response.SuccessRes;
import com.GP.First.Step.auth.JwtUtil;
import com.GP.First.Step.entities.User;
import com.GP.First.Step.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));

            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            String token = jwtUtil.createToken(Optional.of(user));
            LoginRes loginRes = new LoginRes(email, token);

            return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Login successful", loginRes));
        } catch (BadCredentialsException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User newUser) {
        if (userService.getUserByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorRes(HttpStatus.CONFLICT, "Email already in use"));
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userService.saveUser(newUser);

        String token = jwtUtil.createToken(Optional.of(savedUser));
        LoginRes loginRes = new LoginRes(savedUser.getEmail(), token);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessRes(HttpStatus.OK, "Signup successful", loginRes));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Logout successful", null));
    }
}