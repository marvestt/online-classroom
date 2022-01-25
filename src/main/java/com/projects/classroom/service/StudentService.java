package com.projects.classroom.service;

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

import com.projects.classroom.dao.StudentDao;
import com.projects.classroom.exception.DatabaseOperationException;
import com.projects.classroom.model.Student;
import com.projects.classroom.utilities.Utilities;

@Service
public class StudentService {

    @Autowired
    private StudentDao studentDao;
    
    @Autowired
    private UserService userService;

    private static final String STUDENT_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";
    private static final String GET_STUDENT_BY_USER_ID_ERROR_MESSAGE = "";
    private static final String NULL_STUDENT = "";

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private void validate(Student student) throws InvalidObjectException {
        List<String> violations = Utilities.validate(student);
        if (student == null) {
            logger.error("Student object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_STUDENT);
        }
        logger.debug("Validating the following Student object: " + student);
        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error("Validation of the Student object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + student.getStudentId());
        }
    }

    public Student get(long studentId) throws DatabaseOperationException, InvalidObjectException {
        Student student = null;
        try {
            logger.debug("Attempting to retrieve Student object from the database with an studentId = " + studentId);
            student = studentDao.get(studentId);
        } catch (DataAccessException e) {
            logger.error(
                    "The Student could not be found in the database. Check the records to make sure the following studentId is present in the students table: "
                            + studentId);
            throw new DatabaseOperationException(STUDENT_ID_ERROR_MESSAGE);
        }
        return student;
    }

    public Student getStudentByUserId(long userId) throws DatabaseOperationException, InvalidObjectException {
        Student student = null;
        try {
            logger.debug("Attempting to retrieve the Student with userId = " + userId);
            student = studentDao.getStudentByUserId(userId);
        } catch (DataAccessException e) {
            logger.error("No Student with the userId was found. Please check the students table in the database");
            throw new DatabaseOperationException(GET_STUDENT_BY_USER_ID_ERROR_MESSAGE);
        }
        return student;
    }
    
    public Student getStudentByUsername(String username) {
        try {
            return studentDao.getStudentByUserId(userService.getByUsername(username).getUserId());
        } catch (InvalidObjectException | DatabaseOperationException e) {
            e.printStackTrace();
        } 
        return null;
    }
    
    public boolean checkStudentExists(String username) {
        try {
            logger.debug("Attempting to retrieve the Student with username = " + username);
            Student student = getStudentByUsername(username);
            if(student != null) {
                logger.debug("Student exists");
                return true;
            }
        }catch(DatabaseOperationException | EmptyResultDataAccessException e) {
            logger.debug("Student cannot be accessed. Student doens't exist");
        }
        
        return false;
    }

    public Page<Student> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Student> students = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the Students");
            students = studentDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve a page of all Students. Check the Students table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each Student object in the page");
        for (Student student : students) {
            validate(student);
        }

        return students;
    }

    public int save(Student student) {
        int output = 0;
        logger.debug("Attempting to save the following Student object to the database: " + student);
       
        try {
            validate(student);
            output = studentDao.save(student);
        } catch (DataAccessException | DatabaseOperationException e) {
            logger.error(
                    "Failed to save the Student. Check the database to make sure the students table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE + e.getMessage());
        }
        catch(InvalidObjectException e) {
            logger.error("The student object is in an invalid state. Please check all fields to ensure that they are valid\n" + e);
        }
        return output;
    }

    public void update(Student student) {
        logger.debug("Attempting to update the following Student object in the database: " + student);
        try {
            validate(student);
            studentDao.update(student);
        } catch (DataAccessException | DatabaseOperationException e) {
            logger.error("Failed to perform update. Check the Student object and students table in the database and make sure the student is present");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + student.getStudentId());
        }
        catch(InvalidObjectException e ) {
            logger.error("Student object provided has proven to be invalid. Please check fields for validity");
        }
    }

    public void delete(long studentId) throws DatabaseOperationException {
        logger.debug("Attempting to delete Student with studentId = " + studentId);
        try {
            studentDao.delete(studentId);
        } catch (DataAccessException e) {
            logger.error(
                    "Student deletion failed. Check the students table in the database and make sure the correct student_id is used");
            throw new DatabaseOperationException(STUDENT_ID_ERROR_MESSAGE);
        }
    }
}
