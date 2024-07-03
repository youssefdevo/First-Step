package com.GP.First.Step.DTO.request;

// Declares the LoginReq class which is a simple Data Transfer Object (DTO) for handling login requests.
// This class are typically used in  Auth controller to receive data from HTTP requests and pass it to service layers for processing.
public class LoginReq {
    private String email; // Stores the email of the user.
    private String password; // Stores the password of the user.

    // Constructor to initialize LoginReq with email and password.
    public LoginReq(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter method for the email.
    public String getEmail() {
        return email;
    }

    // Setter method for the email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter method for the password.
    public String getPassword() {
        return password;
    }

    // Setter method for the password.
    public void setPassword(String password) {
        this.password = password;
    }
}