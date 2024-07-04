package com.GP.First.Step.controllers;


import com.GP.First.Step.DTO.request.ResetPasswordReq;
import com.GP.First.Step.DTO.response.ErrorRes;
import com.GP.First.Step.services.ProfileService;
import com.GP.First.Step.DTO.response.SuccessRes;
import com.GP.First.Step.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

// Mark this class as a RestController to handle HTTP requests
@RestController
@RequestMapping("/rest/profile")
public class UserProfileController {

    private final ProfileService profileService;

    // Inject ProfileService via constructor
    @Autowired
    public UserProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    // Handle GET requests to retrieve the current user's profile
    @GetMapping
    public ResponseEntity<SuccessRes> getProfile() {
        // Retrieve the email of the currently authenticated user from the security context.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Use the profileService to get the user's profile based on their email.
        User user = profileService.getUserProfileByEmail(email);

        // Return an HTTP 200 response with a success message and the user's profile.
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your profile got successfully", user));
    }

    // Handle GET requests to retrieve a user's profile by email
    @GetMapping("{email}")
    public ResponseEntity<?> getUserProfile(@PathVariable String email) {
        // Use the profileService to get the user's profile based on the provided email.
        Optional<User> user = profileService.getUserProfileByUsername(email);
        if(user.isEmpty()) {
            ErrorRes errorRes = new ErrorRes(HttpStatus.NOT_FOUND, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRes);
        }

        // Return an HTTP 200 response with a success message and the user's profile.
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "User profile got successfully", user));
    }

    // Handle PUT requests to update the current user's profile
    @PutMapping
    public ResponseEntity<SuccessRes> updateProfile(@RequestBody User updatedUser) {
        // Retrieve the email of the currently authenticated user from the security context.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Use the profileService to update the user's profile with the provided updatedUser details.
        User user = profileService.updateProfile(email, updatedUser);

        // Return an HTTP 200 response with a success message and the updated user's profile.
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your profile updated successfully", user));
    }

    // Handle PUT requests to reset the current user's password
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordReq resetPasswordReq) {
        // Retrieve the email of the currently authenticated user from the security context.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            // Use the profileService to reset the user's password with the provided details.
            profileService.resetPassword(email, resetPasswordReq);

            // Return an HTTP 200 response with a success message.
            return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Password reset successfully", null));
        } catch (IllegalArgumentException e) {
            // Handle any IllegalArgumentException (e.g., incorrect current password) by returning a bad request response.
            ErrorRes errorRes = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRes);
        }
    }

    // Handle POST requests to sign out the current user
    @PostMapping("/signout")
    public ResponseEntity<SuccessRes> signOut(HttpServletRequest request) {

        // Clear the security context to sign out the user.
        SecurityContextHolder.clearContext();

        // Return an HTTP 200 response with a success message indicating the user has signed out.
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "User signed out successfully", null));
    }

}