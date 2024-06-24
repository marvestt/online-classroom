package com.projects.classroom.exception;

public class UserAlreadyExistsException  extends RuntimeException{

    private static final String GENERIC_ERROR_MESSAGE = "The user already exists and therefore cannot be created";
    private static final String USER_ALREADY_FOUND_MESSAGE = "The user with the following username already exists:  ";
    
    public UserAlreadyExistsException() {
        super(GENERIC_ERROR_MESSAGE);
    }
    
    public UserAlreadyExistsException(String message) {
        super(USER_ALREADY_FOUND_MESSAGE + message);
    }
}
