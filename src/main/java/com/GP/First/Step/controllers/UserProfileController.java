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

@RestController
@RequestMapping("/rest/profile")
public class UserProfileController {

    private final ProfileService profileService;

    @Autowired
    public UserProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @GetMapping
    public ResponseEntity<SuccessRes> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = profileService.getUserProfileByEmail(email);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your profile got successfully", user));
    }


    @GetMapping("{email}")
    public ResponseEntity<SuccessRes> getUserProfile(@PathVariable String email) {
        User user = profileService.getUserProfileByUsername(email);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "User profile got successfully", user));
    }


    @PutMapping
    public ResponseEntity<SuccessRes> updateProfile(@RequestBody User updatedUser) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = profileService.updateProfile(email, updatedUser);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your profile updated successfully", user));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordReq resetPasswordReq) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            profileService.resetPassword(email, resetPasswordReq);
            return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Password reset successfully", null));
        } catch (IllegalArgumentException e) {
            ErrorRes errorRes = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRes);
        }
    }


    @PostMapping("/signout")
    public ResponseEntity<SuccessRes> signOut(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "User signed out successfully", null));
    }

}