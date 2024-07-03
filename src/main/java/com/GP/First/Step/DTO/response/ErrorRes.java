package com.GP.First.Step.DTO.response;

import org.springframework.http.HttpStatus; // Imports the HttpStatus class.

// Class which is a Data Transfer Object (DTO) for handling error responses.
// Class handles error responses, providing the HTTP status and an error message
public class ErrorRes {
    private HttpStatus httpStatus; // Stores the HTTP status of the error response.
    private String message; // Stores the error message.
    // Constructor to initialize ErrorRes with httpStatus and message.
    public ErrorRes(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    // Getter method for the HTTP status.
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    // Setter method for the HTTP status.
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    // Getter method for the message.
    public String getMessage() {
        return message;
    }

    // Setter method for the message.
    public void setMessage(String message) {
        this.message = message;
    }
}