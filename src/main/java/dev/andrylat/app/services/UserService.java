package dev.andrylat.app.services;

import java.io.InvalidObjectException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import dev.andrylat.app.daos.UserDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Assignment;
import dev.andrylat.app.models.Teacher;
import dev.andrylat.app.models.User;
import dev.andrylat.app.utilities.Utilities;

public class UserService {
    
    @Autowired
    private UserDao userDao;
    
    private static final String USER_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";

    private static final String GET_USER_BY_USERNAME_ERROR_MESSAGE = null;
    
    private void validate(User user) throws InvalidObjectException {
        List<String> violations = Utilities.validate(user);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                    .stream()
                    .collect(Collectors.joining(NEW_LINE));
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + user.getUserId());
        }
    }
    
    public User get(long userId) throws DatabaseOperationException, InvalidObjectException{
        User user = new User();
        try {
            user = userDao.get(userId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(USER_ID_ERROR_MESSAGE);
        }
        validate(user);  
        return user;
    }
    
    public User getUsersByUsername(String username) throws DatabaseOperationException, InvalidObjectException{
        User user = new User();
        try {
            user = userDao.getUserByUsername(username);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_USER_BY_USERNAME_ERROR_MESSAGE);
        }
        validate(user);
        return user;
    }
    
    public Page<User> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<User> users = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            users = userDao.getAll(page);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(User user : users) {
            validate(user);
        }
        return users;
    }
    
    public int save(User user) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(user);
        try {
            output = userDao.save(user);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }
    
    public void update(User user) throws InvalidObjectException, DatabaseOperationException{
        validate(user);
        try {
            userDao.update(user);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + user.getUserId());
        }
    }
    
    public void delete(long userId) throws DatabaseOperationException{
        try {
            userDao.delete(userId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(USER_ID_ERROR_MESSAGE);
        }
    }
}
