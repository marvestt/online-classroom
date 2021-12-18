package dev.andrylat.app.services;

import java.io.InvalidObjectException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dev.andrylat.app.daos.UserDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.User;
import dev.andrylat.app.utilities.Utilities;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    private static final String USER_ID_ERROR_MESSAGE = "The user_id provided doesn't match any columns in the database. Please verify the value and try again";;
    private static final String GET_ALL_ERROR_MESSAGE = "Something went wrong when trying to retrieve all of the users. Please check the database";
    private static final String SAVE_ERROR_MESSAGE = "Something went wrong when trying to save the user object. Please check the database.";
    private static final String UPDATE_ERROR_MESSAGE = "Something went wrong when trying to update the user object. Please check the database"
            + "for the user with userId=";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "User object has an invalid state. Check user properties to make sure they "
            + "aren't empty for the user with userId=";
    private static final String NEW_LINE = "\n";
    private static final String GET_USER_BY_USERNAME_ERROR_MESSAGE = "";
    private static final String NULL_USER = "User is not allowed to be null. Please check the object and make sure a value is assigned correctly";

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private void validate(User user) throws InvalidObjectException {
        List<String> violations = Utilities.validate(user);
        if (user == null) {
            logger.error("User object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_USER);
        }
        logger.debug("Validating the following User object: " + user);
        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error("Validation of the User object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + user.getUserId());
        }
    }

    public User get(long userId) throws DatabaseOperationException, InvalidObjectException {
        User user = null;
        try {
            logger.debug("Attempting to retrieve User object from the database with an userId = " + userId);
            user = userDao.get(userId);
        } catch (DataAccessException e) {
            logger.error(
                    "The User could not be found in the database. Check the records to make sure the following userId is present in the users table: "
                            + userId);
            throw new DatabaseOperationException(USER_ID_ERROR_MESSAGE);
        }
        return user;
    }

    public User getByUsername(String username) throws DatabaseOperationException, InvalidObjectException {
        User user = null;
        try {
            logger.debug("Attempting to retrieve the User with username = " + username);
            user = userDao.getUserByUsername(username);
        } catch (DataAccessException e) {
            logger.error("No User with the username was found. Please check the users table in the database");
            throw new DatabaseOperationException(GET_USER_BY_USERNAME_ERROR_MESSAGE);
        }
        return user;
    }
    
    public boolean checkUserExists(String username) {
        try {
            logger.debug("Checking if user already exists");
            logger.debug("Attempting to retrieve the User with username = " + username);
            User user = userDao.getUserByUsername(username);
            if(user != null) {
                logger.debug("User already exists");
                return true;
            }
        }catch(DatabaseOperationException | EmptyResultDataAccessException e) {
            logger.debug("User cannot be accessed. User doens't exist");
        }
        
        return false;
    }
    
    public boolean checkUserCredentials(User user) {
        if(user.getUsername() == null || user.getPassword() == null) {
            return false;
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean userExists = checkUserExists(user.getUsername());
        if(userExists) {
            String password = user.getPassword();
            String encodedPassword = userDao.getUserPassword(user.getUsername());
            boolean passwordMatches = BCrypt.checkpw(password,encodedPassword);

            return userExists && passwordMatches;
        }
        return false;
        
    }

    public Page<User> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<User> users = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the Users");
            users = userDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve a page of all Users. Check the users table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each User object in the page");
        for (User user : users) {
            validate(user);
        }
        return users;
    }

    public int save(User user) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        logger.debug("Attempting to save the following User object to the database: " + user);
        validate(user);
        try {
            output = userDao.save(user);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to save the User. Check the database to make sure the users table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }

    public void update(User user) throws InvalidObjectException, DatabaseOperationException {
        logger.debug("Attempting to update the following User object in the database: " + user);
        validate(user);
        try {
            userDao.update(user);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to perform update. Check the User object and users table in the database and make sure the user is present");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + user.getUserId());
        }
    }

    public void delete(long userId) throws DatabaseOperationException {
        logger.debug("Attempting to delete User with userId = " + userId);
        try {
            userDao.delete(userId);
        } catch (DataAccessException e) {
            logger.error(
                    "User deletion failed. Check the users table in the database and make sure the correct user_id is used");
            throw new DatabaseOperationException(USER_ID_ERROR_MESSAGE);
        }
    }
    
    public void encryptUserPassword(User user) {
        String password = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
    }
}
