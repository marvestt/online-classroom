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

import dev.andrylat.app.daos.StudentDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Assignment;
import dev.andrylat.app.models.Lesson;
import dev.andrylat.app.models.Student;
import dev.andrylat.app.utilities.Utilities;

public class StudentService {
    
    @Autowired
    private StudentDao studentDao;
    
    private static final String STUDENT_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";

    private static final String GET_STUDENT_BY_USER_ID_ERROR_MESSAGE = null;
    
    private void validate(Student student) throws InvalidObjectException {
        List<String> violations = Utilities.validate(student);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                    .stream()
                    .collect(Collectors.joining(NEW_LINE));
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + student.getStudentId());
        }
    }
    
    public Student get(long studentId) throws DatabaseOperationException, InvalidObjectException{
        Student student = new Student();
        try {
            student = studentDao.get(studentId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(STUDENT_ID_ERROR_MESSAGE);
        }
        validate(student);  
        return student;
    }
    
    public Student getStudentsByUserId(long userId) throws DatabaseOperationException, InvalidObjectException{
        Student student = new Student();
        try {
            student = studentDao.getStudentByUserId(userId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_STUDENT_BY_USER_ID_ERROR_MESSAGE);
        }
        validate(student);
        return student;
    }
    
    public Page<Student> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Student> students = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            students = studentDao.getAll(page);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(Student student : students) {
            validate(student);
        }
        
        return students;
    }
    
    public int save(Student student) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(student);
        try {
            output = studentDao.save(student);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }
    
    public void update(Student student) throws InvalidObjectException, DatabaseOperationException{
        validate(student);
        try {
            studentDao.update(student);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + student.getStudentId());
        }
    }
    
    public void delete(long studentId) throws DatabaseOperationException{
        try {
            studentDao.delete(studentId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(STUDENT_ID_ERROR_MESSAGE);
        }
    }
}
