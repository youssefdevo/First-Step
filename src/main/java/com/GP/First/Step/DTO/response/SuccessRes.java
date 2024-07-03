package com.GP.First.Step.DTO.response;

import org.springframework.http.HttpStatus; // Imports the HttpStatus class.

// Class which is a Data Transfer Object (DTO) for handling successful responses.
// Class handles successful responses, providing the HTTP status, a success message, and any additional data.
public class SuccessRes {
    private HttpStatus status; // Stores the HTTP status of the response.
    private String message; // Stores the success message.
    private Object data; // Stores any additional data related to the success response.

    // Constructor to initialize SuccessRes with status, message, and data.
    public SuccessRes(HttpStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getter method for the status.
    public HttpStatus getStatus() {
        return status;
    }

    // Setter method for the status.
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    // Getter method for the message.
    public String getMessage() {
        return message;
    }

    // Setter method for the message.
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter method for the data.
    public Object getData() {
        return data;
    }

    // Setter method for the data.
    public void setData(Object data) {
        this.data = data;
    }
}

