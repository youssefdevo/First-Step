package com.GP.First.Step.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Like {
    String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
    @Override
    public String toString() {
        return "Like{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
