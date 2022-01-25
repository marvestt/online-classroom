package com.projects.classroom.service;

import java.io.InvalidObjectException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projects.classroom.dao.ClassMembersDao;
import com.projects.classroom.dao.ClassroomDao;
import com.projects.classroom.exception.DatabaseOperationException;
import com.projects.classroom.model.Classroom;
import com.projects.classroom.utilities.Utilities;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomDao classroomDao;
    
    @Autowired
    private ClassMembersDao classMembersDao;

    private static final String CLASS_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";
    private static final String GET_CLASSES_BY_MAIN_TEACHER_ID_ERROR_MESSAGE = "";
    private static final String NULL_CLASSROOM = "";

    private static final Logger logger = LoggerFactory.getLogger(ClassroomService.class);

    private void validate(Classroom classroom) throws InvalidObjectException {
        List<String> violations = Utilities.validate(classroom);
        if (classroom == null) {
            logger.error("Classroom object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_CLASSROOM);
        }
        logger.debug("Validating the following classroom object: " + classroom);
        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error("Validation of the Classroom object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + classroom.getClassId());
        }
    }

    public Classroom get(long classId) throws DatabaseOperationException, InvalidObjectException {
        Classroom classroom = null;
        try {
            logger.debug("Attempting to retrieve Classroom object from the database with an classId = " + classId);
            classroom = classroomDao.get(classId);
        } catch (DataAccessException e) {
            logger.error(
                    "The Classroom could not be found in the database. Check the records to make sure the following classId is present in the classes table: "
                            + classId);
            throw new DatabaseOperationException(CLASS_ID_ERROR_MESSAGE);
        }
        return classroom;
    }

    public List<Classroom> getClassesByMainTeacherId(long mainTeacherId)
            throws DatabaseOperationException, InvalidObjectException {
        List<Classroom> classrooms = Collections.EMPTY_LIST;
        try {
            logger.debug(
                    "Attempting to retrieve a list of all Classrooms that have the mainTeacherId = " + mainTeacherId);
            classrooms = classroomDao.getClassesByMainTeacherId(mainTeacherId);
        } catch (DataAccessException e) {
            logger.error(
                    "No Classrooms with the mainTeacherId were found. Please check the classes table in the database");
            throw new DatabaseOperationException(GET_CLASSES_BY_MAIN_TEACHER_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each Classroom object in the list");
        for (Classroom classroom : classrooms) {
            validate(classroom);
        }
        return classrooms;
    }
    
    public List<Classroom> getClassroomsByUserId(Long userId) throws DatabaseOperationException{
        logger.debug("Attempting to retrieve class ids for user");
        List<Classroom> classrooms = Collections.EMPTY_LIST;
        try {
           classrooms = classMembersDao.getClassroomIdsByUserId(userId)
                                       .stream()
                                       .map(e -> {try{return get(e);} catch(InvalidObjectException ex) {return null;}})
                                       .filter(e -> e != null)
                                       .collect(Collectors.toList());
        }
        catch(DataAccessException e) {
            logger.error("An error occured when attempting to access the class_members table. Please check the sql tables");
            throw new DatabaseOperationException(e.toString());
        }
        return classrooms;
    }
    
    public List<Classroom> searchClassrooms(String searchText) throws DatabaseOperationException{
        logger.debug("Attempting to retrieve classroom objects that match the search text");
        List<Classroom> classrooms = Collections.EMPTY_LIST;
        try {
           classrooms  = classroomDao.searchClassrooms(searchText);
        }
        catch(DataAccessException e) {
            logger.error("An error occured when attempting to access the classes table. Please check the sql tables");
            throw new DatabaseOperationException(e.toString());
        }
        return classrooms;
    }
    
    public void registerUserInClassroom(long userId, long classId) throws DatabaseOperationException{
        logger.debug("Attempting to register user into class");
        try {
            classMembersDao.addUserToClassroom(userId, classId);
        }
        catch(DataAccessException e) {
            logger.error("An error occured when trying to register user into the class. Please check the class_members table in the database");
            throw new DatabaseOperationException(e.toString());
        }
    }

    public Page<Classroom> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Classroom> classrooms = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the Classrooms");
            classrooms = classroomDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve a page of all Classrooms. Check the classes table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each Classroom object in the page");
        for (Classroom classroom : classrooms) {
            validate(classroom);
        }
        return classrooms;
    }

    public int save(Classroom classroom) {
        logger.debug("Attempting to update the following Classroom object in the database: " + classroom);
        int output = 0;
        
        try {
            validate(classroom);
            output = classroomDao.save(classroom);
        } catch (DatabaseOperationException e) {
            logger.error(
                    "Failed to save the Classroom. Check the database to make sure the classes table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        catch(InvalidObjectException e) {
            logger.error("Classroom object seems to be in an invalid state. Please verify the classroom object\n" + e );
        }
        return output;
    }

    public void update(Classroom classroom)  {
        logger.debug("Attempting to update the following Classroom object in the database: " + classroom);
        
        try {
            validate(classroom);
            classroomDao.update(classroom);
        } catch (DataAccessException | InvalidObjectException | DatabaseOperationException e) {
            logger.error("Failed to perform update. Check the Classroom object and classes table in the database");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + classroom.getClassId());
        }
    }

    public void delete(long classId) {
        logger.debug("Attempting to delete Classroom with classId = " + classId);
        try {
            classroomDao.delete(classId);
        } catch (DataAccessException | DatabaseOperationException e) {
            logger.error(
                    "Classroom deletion failed. Check the classes table in the database and make sure the correct class_id is used");
            throw new DatabaseOperationException(CLASS_ID_ERROR_MESSAGE);
        }
    }
    
    public void removeFromClassroom(long classId, long userId) {
        logger.debug("Attempting to delete class member with classId = " + classId + " and userId: " + userId);
        try {
            classMembersDao.delete(classId, userId);
        } catch (DataAccessException | DatabaseOperationException e) {
            logger.error(
                    "Class member deletion failed. Check the class_members table in the database and make sure the correct class_id is used");
            throw new DatabaseOperationException(CLASS_ID_ERROR_MESSAGE);
        }
    }
}
