package com.projects.classroom.models;


public class UserOption {

    private UserType userType;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    @Override
    public String toString() {
        return String.format("UserOption[userType = %s]", userType);
    }
}
