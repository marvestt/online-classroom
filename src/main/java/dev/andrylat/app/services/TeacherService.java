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

import dev.andrylat.app.daos.TeacherDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Assignment;
import dev.andrylat.app.models.Student;
import dev.andrylat.app.models.Teacher;
import dev.andrylat.app.utilities.Utilities;

public class TeacherService {
    
    @Autowired
    private TeacherDao teacherDao;
    
    private static final String TEACHER_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";

    private static final String GET_TEACHER_BY_USER_ID_ERROR_MESSAGE = null;
    
    private void validate(Teacher teacher) throws InvalidObjectException {
        List<String> violations = Utilities.validate(teacher);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                    .stream()
                    .collect(Collectors.joining(NEW_LINE));
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + teacher.getTeacherId());
        }
    }
    
    public Teacher get(long teacherId) throws DatabaseOperationException, InvalidObjectException{
        Teacher teacher = new Teacher();
        try {
            teacher = teacherDao.get(teacherId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(TEACHER_ID_ERROR_MESSAGE);
        }
        validate(teacher);  
        return teacher;
    }
    
    public Teacher getTeachersByUserId(long userId) throws DatabaseOperationException, InvalidObjectException{
        Teacher teacher = new Teacher();
        try {
            teacher = teacherDao.getTeacherByUserId(userId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_TEACHER_BY_USER_ID_ERROR_MESSAGE);
        }
        validate(teacher);
        return teacher;
    }
    
    public Page<Teacher> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Teacher> teachers = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            teachers = teacherDao.getAll(page);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(Teacher teacher : teachers) {
            validate(teacher);
        }
        
        return teachers;
    }
    
    public int save(Teacher teacher) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(teacher);
        try {
            output = teacherDao.save(teacher);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }
    
    public void update(Teacher teacher) throws InvalidObjectException, DatabaseOperationException{
        validate(teacher);
        try {
            teacherDao.update(teacher);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + teacher.getTeacherId());
        }
    }
    
    public void delete(long teacherId) throws DatabaseOperationException{
        try {
            teacherDao.delete(teacherId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(TEACHER_ID_ERROR_MESSAGE);
        }
    }
}
