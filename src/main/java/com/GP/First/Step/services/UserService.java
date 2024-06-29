package com.GP.First.Step.services;

import com.GP.First.Step.DAO.UserRepository;
import com.GP.First.Step.entities.User;
import org.springframework.beans.factory.annotation.Autowired; // annotation for dependency injection
import org.springframework.stereotype.Service;

import java.util.Optional;// Imports the Optional class for handling potential null values

@Service //service component in Spring, making it eligible for component scanning and dependency injection
public class UserService {

    private final UserRepository userRepository;

    @Autowired // Indicates that this constructor should be used for dependency injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; // Initializes the userRepository instance
    }
    // Retrieves a User by their email,using Optional to handle the case where no user is found
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Retrieves a User by their ID, using Optional to handle the case where no user is found
    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    // Saves a User entity to the repository and returns the saved entity
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Deletes a User from the repository based using his ID
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }




}
