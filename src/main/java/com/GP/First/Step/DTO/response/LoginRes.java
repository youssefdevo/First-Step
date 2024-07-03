package com.GP.First.Step.DTO.response;

// Class which is a Data Transfer Object (DTO) for handling login responses.
// Class handles responses for login requests, providing the email and authentication token.
public class LoginRes {

    private String email; // Stores the email of the user.
    private String token; // Stores the authentication token for the user.

    // Constructor to initialize LoginRes with email and token.
    public LoginRes(String email, String token) {
        this.email = email;
        this.token = token;
    }
    // Getter method for the email.
    public String getEmail() {
        return email;
    }

    // Setter method for the email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter method for the token.
    public String getToken() {
        return token;
    }

    // Setter method for the token.
    public void setToken(String token) {
        this.token = token;
    }
}