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

import dev.andrylat.app.daos.LessonDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Assignment;
import dev.andrylat.app.models.Lesson;
import dev.andrylat.app.utilities.Utilities;

public class LessonService {

    @Autowired
    private LessonDao lessonDao;
    
    private static final String LESSON_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";

    private static final String GET_LESSONS_BY_CLASS_ID_ERROR_MESSAGE = null;
    
    private void validate(Lesson lesson) throws InvalidObjectException {
        List<String> violations = Utilities.validate(lesson);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                    .stream()
                    .collect(Collectors.joining(NEW_LINE));
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + lesson.getLessonId());
        }
    }
    
    public Lesson get(long lessonId) throws DatabaseOperationException, InvalidObjectException{
        Lesson lesson = new Lesson(); 
        try {
            lesson = lessonDao.get(lessonId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(LESSON_ID_ERROR_MESSAGE);
        }
        validate(lesson);  
        return lesson;
    }
    
    public List<Lesson> getLessonsByClassId(long classId) throws DatabaseOperationException, InvalidObjectException{
        List<Lesson> lessons = Collections.EMPTY_LIST;
        try {
            lessons = lessonDao.getLessonsByClassId(classId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_LESSONS_BY_CLASS_ID_ERROR_MESSAGE);
        }
        for(Lesson lesson : lessons) {
            validate(lesson);
        }
        return lessons;
    }
    
    public Page<Lesson> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Lesson> lessons = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            lessons = lessonDao.getAll(page);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(Lesson lesson : lessons) {
            validate(lesson);
        }
        
        return lessons;
    }
    
    public int save(Lesson lesson) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(lesson);
        try {
            output = lessonDao.save(lesson);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }
    
    public void update(Lesson lesson) throws InvalidObjectException, DatabaseOperationException{
        validate(lesson);
        try {
            lessonDao.update(lesson);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + lesson.getLessonId());
        }
    }
    
    public void delete(long lessonId) throws DatabaseOperationException{
        try {
            lessonDao.delete(lessonId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(LESSON_ID_ERROR_MESSAGE);
        }
    }
}
