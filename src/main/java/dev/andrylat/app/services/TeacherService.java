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
import org.springframework.stereotype.Service;

import dev.andrylat.app.daos.TeacherDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Teacher;
import dev.andrylat.app.utilities.Utilities;

@Service
public class TeacherService {

    @Autowired
    private TeacherDao teacherDao;
    
    @Autowired
    private UserService userService;

    private static final String TEACHER_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";
    private static final String GET_TEACHER_BY_USER_ID_ERROR_MESSAGE = "";
    private static final String NULL_TEACHER = "";

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private void validate(Teacher teacher) throws InvalidObjectException {
        List<String> violations = Utilities.validate(teacher);
        if (teacher == null) {
            logger.error("Teacher object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_TEACHER);
        }
        logger.debug("Validating the following Teacher object: " + teacher);
        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error("Validation of the Teacher object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + teacher.getTeacherId());
        }
    }

    public Teacher get(long teacherId) throws DatabaseOperationException, InvalidObjectException {
        Teacher teacher = null;
        try {
            logger.debug("Attempting to retrieve Teacher object from the database with an teacherId = " + teacherId);
            teacher = teacherDao.get(teacherId);
        } catch (DataAccessException e) {
            logger.error(
                    "The Teacher could not be found in the database. Check the records to make sure the following teacherId is present in the teachers table: "
                            + teacherId);
            throw new DatabaseOperationException(TEACHER_ID_ERROR_MESSAGE);
        }
        return teacher;
    }

    public Teacher getTeachersByUserId(long userId) throws DatabaseOperationException, InvalidObjectException {
        Teacher teacher = null;
        try {
            logger.debug("Attempting to retrieve the Teacher with userId = " + userId);
            teacher = teacherDao.getTeacherByUserId(userId);
        } catch (DataAccessException e) {
            logger.error("No Teacher with the userId was found. Please check the teachers table in the database");
            throw new DatabaseOperationException(GET_TEACHER_BY_USER_ID_ERROR_MESSAGE);
        }
        return teacher;
    }

    public Page<Teacher> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Teacher> teachers = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the Teachers");
            teachers = teacherDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve a page of all Teachers. Check the teachers table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each Teacher object in the page");
        for (Teacher teacher : teachers) {
            validate(teacher);
        }

        return teachers;
    }
    
    public Teacher getTeacherByUsername(String username) {
        try {
            return teacherDao.getTeacherByUserId(userService.getByUsername(username).getUserId());
        } catch (InvalidObjectException | DatabaseOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean checkTeacherExists(String username) {
        try {
            logger.debug("Attempting to retrieve the Student with username = " + username);
            Teacher teacher = getTeacherByUsername(username);
            if(teacher != null) {
                logger.debug("Teacher exists");
                return true;
            }
        }catch(DatabaseOperationException | EmptyResultDataAccessException e) {
            logger.debug("Teacher cannot be accessed. Teacher doens't exist");
        }
        
        return false;
    }

    public int save(Teacher teacher) {
        int output = 0;
        logger.debug("Attempting to save the following Teacher object to the database: " + teacher);
        
        try {
            validate(teacher);
            output = teacherDao.save(teacher);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to save the Teacher. Check the database to make sure the teachers table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        catch(InvalidObjectException e) {
            logger.error("Teacher object is invalid, please check state\n" + e );
        }
        return output;
    }

    public void update(Teacher teacher) throws InvalidObjectException, DatabaseOperationException {
        logger.debug("Attempting to update the following Teacher object in the database: " + teacher);
        validate(teacher);
        try {
            teacherDao.update(teacher);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to perform update. Check the Teacher object and teachers table in the database and make sure the teacher is present");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + teacher.getTeacherId());
        }
    }

    public void delete(long teacherId) throws DatabaseOperationException {
        logger.debug("Attempting to delete Teacher with teacherId = " + teacherId);
        try {
            teacherDao.delete(teacherId);
        } catch (DataAccessException e) {
            logger.error(
                    "Teacher deletion failed. Check the teachers table in the database and make sure the correct teacher_id is used");
            throw new DatabaseOperationException(TEACHER_ID_ERROR_MESSAGE);
        }
    }
}
