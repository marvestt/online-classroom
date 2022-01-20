package dev.andrylat.app.exceptions;

import java.sql.SQLException;

public class DatabaseOperationException extends RuntimeException{

    public DatabaseOperationException(String errorMessage) {
        super(errorMessage);
    }
    
    public DatabaseOperationException(SQLException exception) {
        super(exception);
    }
}
