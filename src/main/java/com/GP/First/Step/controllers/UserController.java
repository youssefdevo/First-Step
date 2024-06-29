package com.GP.First.Step.controllers;

import com.GP.First.Step.entities.User;
import com.GP.First.Step.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;     // Annotation for dependency injection
import org.springframework.web.bind.annotation.PathVariable;   // Imports the PathVariable annotation to extract data from the URI
import org.springframework.web.bind.annotation.RequestMapping; // Imports the RequestMapping annotation to map web requests
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    private final UserService userService;

    // Indicates that this constructor should be used for dependency injection
    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;  // Initializes the userService instance
    }

   // Retrieve a User entity by its ID
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // Throws an exception if the user is not found
    }
}
