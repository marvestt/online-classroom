package com.projects.classroom.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.executable.ValidateOnExecution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.projects.classroom.exception.EntityNotFoundException;
import com.projects.classroom.exception.UserAlreadyExistsException;
import com.projects.classroom.model.User;
import com.projects.classroom.repository.UserRepo;

@Service
public class UserService {

   @Autowired
   private UserRepo userRepo;
   
   @Autowired
   PasswordEncryptionService passwordEncryptionService;
   
   private static final String USER_RETRIEVAL_ERROR_MESSAGE = "User with ID ";
   private static final String USERNAME_RETRIEVAL_ERROR_MESSAGE = "User with username ";

    public User get(long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if(userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new EntityNotFoundException(USER_RETRIEVAL_ERROR_MESSAGE + userId);
    }

    public User getByUsername(String username){
       Optional<User> userOpt = userRepo.findByUsername(username);
       if(userOpt.isPresent()) {
           return userOpt.get();
       }
       throw new EntityNotFoundException(USERNAME_RETRIEVAL_ERROR_MESSAGE + username);
    }
    
    public boolean checkUserExists(String username) {
        boolean userExists = userRepo.findByUsername(username).isPresent();
        return userExists;
    }

    public List<User> getAll() {
        List<User> users = userRepo.findAll();
        return users;
    }

    public void delete(long userId) {
        userRepo.deleteById(userId);
    }
    
    public User registerUser(@Valid User user) {
        boolean userExists = checkUserExists(user.getUsername());
        if(!userExists) {
            String rawPassword = user.getPassword();
            String encodedPassword = passwordEncryptionService.encryptPassword(rawPassword);
            user.setPassword(encodedPassword);
            return userRepo.save(user);
        }
        else {
            throw new UserAlreadyExistsException(user.getUsername());    
        }
        
    }
    
    public User save(@Valid User user) {
        return userRepo.save(user);
    }
    
    public boolean checkUserCredentials(String username, String rawPassword) {
        User user = getByUsername(username);
        String encodedPassword = user.getPassword();
        return passwordEncryptionService.checkPassword(rawPassword, encodedPassword);
    }
    

}
