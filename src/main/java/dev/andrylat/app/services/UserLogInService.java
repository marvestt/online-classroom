package dev.andrylat.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.andrylat.app.models.User;

@Service
public class UserLogInService {

    @Autowired
    private UserService userService;
    
    private static final String EMPTY_USERNAME = "Username cannot be empty! Please enter a valid username";
    private static final String EMPTY_PASSWORD = "You need a valid password in order to sign in to your account";
    private static final String INVALID_USER = "Username or password is incorrect. Please check your credentials and try again";
    
    public List<String> validateUser(User user){
        List<String> validationMessages = new ArrayList<>();
        String username = user.getUsername().trim();
        String password = user.getPassword().trim();
        
        if(username == null || username.isBlank()) {
            validationMessages.add(EMPTY_USERNAME);
        }
        
        if(password == null || password.isBlank()) {
            validationMessages.add(EMPTY_PASSWORD);
        }
        if(validationMessages.isEmpty() && !userService.checkUserCredentials(user)) {
            validationMessages.add(INVALID_USER);
        }
        return validationMessages;
        
    }
}
