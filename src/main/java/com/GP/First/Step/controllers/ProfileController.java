package com.GP.First.Step.controllers;


import com.GP.First.Step.DAO.UserRepository;
import com.GP.First.Step.DTO.request.ResetPasswordReq;
import com.GP.First.Step.auth.TokenBlacklist;
import com.GP.First.Step.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklist tokenBlacklist;

    public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenBlacklist tokenBlacklist) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenBlacklist = tokenBlacklist;
    }

    @GetMapping
    public ResponseEntity<User> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateProfile(@RequestBody User updatedUser) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getFirstName() != null)
            user.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null)
            user.setLastName(updatedUser.getLastName());
        if (updatedUser.getUserName() != null)
            user.setUserName(updatedUser.getUserName());
        if (updatedUser.getEmail() != null)
            user.setEmail(updatedUser.getEmail());

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordReq resetPasswordReq) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(resetPasswordReq.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordReq.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        tokenBlacklist.add(token);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User signed out successfully");
    }

}