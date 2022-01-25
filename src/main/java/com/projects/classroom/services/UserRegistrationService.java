package com.projects.classroom.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.classroom.models.User;

@Service
public class UserRegistrationService {
    
    @Autowired
    private UserService userService;
    
    private static final String EMPTY_USERNAME = "Username cannot be empty! Please enter a valid username";
    private static final String USERNAME_ALREADY_EXISTS = "A user already exists with that username. Please enter a valid username for registration";
    private static final String EMPTY_PASSWORD = "You need a valid password in order  to register for a new account";
    private static final String EMPTY_NAME = "A valid first and last name are required in order to continue with registration";
    private static final String SHORT_USERNAME = "Username must be at least 4 characters long";
    private static final String LONG_USERNAME_LENGTH = "Username must be at most 15 characters long";
    private static final  String INVALID_PASSWORD_LENGTH = "Password must be at least 5 characters long";
    private static final String INVALID_NAME_LENGTH = "First and last name must be at least 3 characters long";
    
    public List<String> validateUser(User user){
        List<String> validationMessages = new ArrayList<>();
        String username = user.getUsername().trim();
        String firstName = user.getFirstName().trim();
        String surname = user.getSurname().trim();
        String password = user.getPassword().trim();
        if(username == null || username.isBlank()) {
            validationMessages.add(EMPTY_USERNAME);
        }
        else if(username.length() < 4 ) {
            validationMessages.add(SHORT_USERNAME);
        }
        else if(username.length() > 15) {
            validationMessages.add(LONG_USERNAME_LENGTH);
        }
        else if(userService.checkUserExists(username)){
            validationMessages.add(USERNAME_ALREADY_EXISTS);
        }
        
        if(password == null || password.isBlank()) {
            validationMessages.add(EMPTY_PASSWORD);
        }
        else if(password.length() < 4) {
            validationMessages.add(INVALID_PASSWORD_LENGTH);
        }
        if(firstName == null || surname == null || firstName.isBlank() || surname.isBlank()) {
            validationMessages.add(EMPTY_NAME);
        }
        else if(firstName.length() < 3 || surname.length() < 3) {
            validationMessages.add(INVALID_NAME_LENGTH);
        }
        return validationMessages;
        
    }
    

}
