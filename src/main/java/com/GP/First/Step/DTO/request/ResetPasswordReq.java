package com.GP.First.Step.DTO.request;

// Declares the ResetPasswordReq class which is a simple Data Transfer Object (DTO) for handling password reset requests.
public class ResetPasswordReq {
    private String currentPassword; // Stores the current password of the user.
    private String newPassword; // Stores the new password the user wants to set.


    // Default constructor.
    public ResetPasswordReq() {
    }

    // Constructor to initialize ResetPasswordReq with current and new passwords.
    public ResetPasswordReq(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    // Getter method for the current password.
    public String getCurrentPassword() {
        return currentPassword;
    }

    // Setter method for the current password.
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    // Getter method for the new password.
    public String getNewPassword() {
        return newPassword;
    }

    // Setter method for the new password.
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}