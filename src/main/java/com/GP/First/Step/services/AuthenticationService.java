package com.GP.First.Step.services;

import com.GP.First.Step.entities.User;
import com.GP.First.Step.DAO.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling authentication-related operations.
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for injecting dependencies.
     *
     * @param authenticationManager The authentication manager to authenticate user credentials.
     * @param userRepository        The repository for accessing user data.
     * @param passwordEncoder       The encoder for encoding user passwords.
     */

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user using the provided email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return The authenticated user.
     * @throws BadCredentialsException If the authentication fails.
     */
    public User authenticate(String email, String password) {
        // Authenticate the user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        // Get the authenticated email
        String authenticatedEmail = authentication.getName();

        // Retrieve the user by email
        return userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    /**
     * Creates a new user by encoding the password and saving the user to the repository.
     *
     * @param newUser The new user to be created.
     * @return The saved user.
     */
    public User createUser(User newUser) {
        // Encode the user's password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // Save the user to the repository
        return userRepository.save(newUser);
    }


}
