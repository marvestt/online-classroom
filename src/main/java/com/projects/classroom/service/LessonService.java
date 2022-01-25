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

import com.projects.classroom.dao.LessonDao;
import com.projects.classroom.exception.DatabaseOperationException;
import com.projects.classroom.model.Lesson;
import com.projects.classroom.utilities.Utilities;

@Service
public class LessonService {

    @Autowired
    private LessonDao lessonDao;

    private static final String LESSON_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";
    private static final String GET_LESSONS_BY_CLASS_ID_ERROR_MESSAGE = "";
    private static final String NULL_LESSON = "";

    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

    private void validate(Lesson lesson) throws InvalidObjectException {
        List<String> violations = Utilities.validate(lesson);
        if (lesson == null) {
            logger.error("Lesson object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_LESSON);
        }
        logger.debug("Validating the following lesson object: " + lesson);
        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error("Validation of the Lesson object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + lesson.getLessonId());
        }
    }

    public Lesson get(long lessonId) throws DatabaseOperationException, InvalidObjectException {
        Lesson lesson = null;
        try {
            logger.debug("Attempting to retrieve Lesson object from the database with an lessonId = " + lessonId);
            lesson = lessonDao.get(lessonId);
        } catch (DataAccessException e) {
            logger.error(
                    "The Lesson could not be found in the database. Check the records to make sure the following lessonId is present in the lessons table: "
                            + lessonId);
            throw new DatabaseOperationException(LESSON_ID_ERROR_MESSAGE);
        }
        return lesson;
    }

    public List<Lesson> getLessonsByClassId(long classId) throws DatabaseOperationException, InvalidObjectException {
        List<Lesson> lessons = Collections.EMPTY_LIST;
        try {
            logger.debug(
                    "Attempting to retrieve a list of all Lessons that have the classId = " + classId);
            lessons = lessonDao.getLessonsByClassId(classId);
        } catch (DataAccessException e) {
            logger.error(
                    "No Lessons with the classId were found. Please check the lessons table in the database");
            throw new DatabaseOperationException(GET_LESSONS_BY_CLASS_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each Lesson object in the list");
        for (Lesson lesson : lessons) {
            validate(lesson);
        }
        return lessons;
    }

    public Page<Lesson> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Lesson> lessons = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the Lessons");
            lessons = lessonDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve a page of all Lessons. Check the Lessons table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each Lesson object in the page");
        for (Lesson lesson : lessons) {
            validate(lesson);
        }
        return lessons;
    }

    public int save(Lesson lesson) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        logger.debug("Attempting to save the following Lesson object to the database: " + lesson);
        validate(lesson);
        try {
            output = lessonDao.save(lesson);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to save the Lesson. Check the database to make sure the lessons table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }

    public void update(Lesson lesson) throws InvalidObjectException, DatabaseOperationException {
        logger.debug("Attempting to update the following Lesson object in the database: " + lesson);
        validate(lesson);
        try {
            lessonDao.update(lesson);
        } catch (DataAccessException e) {
            logger.error("Failed to perform update. Check the Lesson object and lessons table in the database and make sure the lesson is present");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + lesson.getLessonId());
        }
    }

    public void delete(long lessonId) throws DatabaseOperationException {
        logger.debug("Attempting to delete Lesson with lessonId = " + lessonId);
        try {
            lessonDao.delete(lessonId);
        } catch (DataAccessException e) {
            logger.error(
                    "Lesson deletion failed. Check the lessons table in the database and make sure the correct lesson_id is used");
            throw new DatabaseOperationException(LESSON_ID_ERROR_MESSAGE);
        }
    }
}
