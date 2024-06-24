package com.projects.classroom.exception;

public class EntityNotFoundException extends RuntimeException{

    private static final String GENERIC_ERROR_MESSAGE = "The entity could not be found";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "The following entity could not be retrieved or found from the database: ";
    
    public EntityNotFoundException() {
        super(GENERIC_ERROR_MESSAGE);
    }
    
    public EntityNotFoundException(String message) {
        super(ENTITY_NOT_FOUND_MESSAGE + message);
    }
}
