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

import dev.andrylat.app.daos.ClassroomDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Assignment;
import dev.andrylat.app.models.Classroom;
import dev.andrylat.app.utilities.Utilities;

public class ClassroomService {

    @Autowired
    private ClassroomDao classroomDao;
    
    private static final String CLASS_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";

    private static final String GET_CLASSES_BY_MAIN_TEACHER_ID_ERROR_MESSAGE = null;
    
    private void validate(Classroom classroom) throws InvalidObjectException {
        List<String> violations = Utilities.validate(classroom);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                .stream()
                .collect(Collectors.joining(NEW_LINE));
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + classroom.getClassId());
        }
    }
    
    public Classroom get(long classId) throws DatabaseOperationException, InvalidObjectException{
        Classroom classroom = new Classroom();
        try {
            classroom = classroomDao.get(classId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(CLASS_ID_ERROR_MESSAGE);
        }
        validate(classroom);  
        return classroom;
    }
    
    public List<Classroom> getClassesByMainTeacherId(long mainTeacherId) throws DatabaseOperationException, InvalidObjectException{
        List<Classroom> classrooms = Collections.EMPTY_LIST;
        try {
            classrooms = classroomDao.getClassesByMainTeacherId(mainTeacherId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_CLASSES_BY_MAIN_TEACHER_ID_ERROR_MESSAGE);
        }
        for(Classroom classroom : classrooms) {
            validate(classroom);
        }
        return classrooms;
    }
    
    public Page<Classroom> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Classroom> classrooms = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            classrooms = classroomDao.getAll(page);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(Classroom classroom : classrooms) {
            validate(classroom);
        }
        return classrooms;
    }
    
    public int save(Classroom classroom) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(classroom);
        try {
            output = classroomDao.save(classroom);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }
    
    public void update(Classroom classroom) throws InvalidObjectException, DatabaseOperationException{
        validate(classroom);
        try {
            classroomDao.update(classroom);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + classroom.getClassId());
        }
    }
    
    public void delete(long classId) throws DatabaseOperationException{
        try {
            classroomDao.delete(classId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(CLASS_ID_ERROR_MESSAGE);
        }
    }
}
