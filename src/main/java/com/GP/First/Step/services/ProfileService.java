package com.GP.First.Step.services;

import com.GP.First.Step.DAO.UserRepository;
import com.GP.First.Step.DTO.request.ResetPasswordReq;
import com.GP.First.Step.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Mark this class as a Service to handle business logic
@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Inject UserRepository and PasswordEncoder via constructor
    @Autowired
    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }
    // Get user profile by user email
    public User getUserProfileByEmail(String email) {
        // It will get the profile of the user if not exist will return exception
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Get user profile by username
    public Optional<User> getUserProfileByUsername(String username) {
        // It will get the profile of the user if not exist will return exception
        return userRepository.findByUserName(username);
    }

    public User updateProfile(String email, User updatedUser) {
        // Retrieve the user by email from the user repository.
        // If the user is not found, throw a RuntimeException with the message "User not found".
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Update the user's profile data using the updatedUser object.

        updateProfileData(user, updatedUser);

        // Save the updated user back to the repository and return the saved user.
        return userRepository.save(user);
    }

    // This method ensures that only non-null fields in the updatedUser object are used to update the corresponding fields in the existing user object.
    private void updateProfileData(User user, User updatedUser) {
        // Update the user's first name if the updated user's first name is not null.
        if (updatedUser.getFirstName() != null)
            user.setFirstName(updatedUser.getFirstName());

        // Update the user's last name if the updated user's last name is not null.
        if (updatedUser.getLastName() != null)
            user.setLastName(updatedUser.getLastName());

        // Update the user's username if the updated user's username is not null.
        if (updatedUser.getUserName() != null)
            user.setUserName(updatedUser.getUserName());
    }

    public void resetPassword(String email, ResetPasswordReq resetPasswordReq) {
        // Retrieve the user by email from the user repository.
        // If the user is not found, throw a RuntimeException with the message "User not found".
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the current password provided in resetPasswordReq matches the user's current password.
        // If not, throw an IllegalArgumentException with the message "Current password is incorrect".
        if (!passwordEncoder.matches(resetPasswordReq.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        // Set the user's password to the new password provided in resetPasswordReq, encoded using the password encoder.
        user.setPassword(passwordEncoder.encode(resetPasswordReq.getNewPassword()));

        // Save the updated user back to the repository.
        userRepository.save(user);
    }

}
