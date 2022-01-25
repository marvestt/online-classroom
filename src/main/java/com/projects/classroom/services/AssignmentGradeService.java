package com.projects.classroom.services;

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

import com.projects.classroom.daos.AssignmentGradeDao;
import com.projects.classroom.exceptions.DatabaseOperationException;
import com.projects.classroom.models.AssignmentGrade;
import com.projects.classroom.utilities.Utilities;

@Service
public class AssignmentGradeService {

    @Autowired
    private AssignmentGradeDao assignmentGradeDao;

    private static final String ASSIGNMENT_GRADE_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String GET_GRADES_BY_ASSIGNMENT_ID_ERROR_MESSAGE = "";
    private static final String GET_GRADES_BY_STUDENT_ID_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";
    private static final String NULL_ASSIGNMENT_GRADE = "";

    private static final Logger logger = LoggerFactory.getLogger(AssignmentGradeService.class);

    private void validate(AssignmentGrade assignmentGrade) throws InvalidObjectException {
        if (assignmentGrade == null) {
            logger.error("AssignmentGrade object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_ASSIGNMENT_GRADE);
        }
        logger.debug("Validating the following AssignmentGrade object: " + assignmentGrade);
        List<String> violations = Utilities.validate(assignmentGrade);

        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error(
                    "Validation of the AssignmentGrade object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(
                    violationMessages + INVALID_OBJECT_ERROR_MESSAGE + assignmentGrade.getAssignmentGradeId());
        }
    }

    public AssignmentGrade get(long assignmentGradeId) throws DatabaseOperationException, InvalidObjectException {
        AssignmentGrade assignmentGrade = null;
        try {
            logger.debug("Attempting to retrieve AssignmentGrade object from the database with an assignmentGradeId = "
                    + assignmentGradeId);
            assignmentGrade = assignmentGradeDao.get(assignmentGradeId);
        } catch (DataAccessException e) {
            logger.error(
                    "The AssignmentGrade could not be found in the database. Check the records to make sure the following assignmentGradeId is present: "
                            + assignmentGradeId);
            throw new DatabaseOperationException(ASSIGNMENT_GRADE_ID_ERROR_MESSAGE);
        }
        return assignmentGrade;
    }

    public List<AssignmentGrade> getAssignementGradesByAssignmentId(long assignmentId)
            throws DatabaseOperationException, InvalidObjectException {
        List<AssignmentGrade> assignmentGrades = Collections.EMPTY_LIST;
        try {
            logger.debug("Attempting to retrieve a list of all AssignmentGrades that have the assignmentId = "
                    + assignmentId);
            assignmentGrades = assignmentGradeDao.getAssignmentGradesByAssignmentId(assignmentId);
        } catch (DataAccessException e) {
            logger.error("No AssignmentGrades with the assignmentId were found. Please check the database");
            throw new DatabaseOperationException(GET_GRADES_BY_ASSIGNMENT_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each AssignmentGrade object in the list");
        for (AssignmentGrade assignmentGrade : assignmentGrades) {
            validate(assignmentGrade);
        }
        return assignmentGrades;
    }

    public List<AssignmentGrade> getAssignementGradesByStudentId(long studentId)
            throws DatabaseOperationException, InvalidObjectException {
        List<AssignmentGrade> assignmentGrades = Collections.emptyList();
        try {
            logger.debug(
                    "Attempting to retrieve a list of all AssignmentGrades that have the studentId = " + studentId);
            assignmentGrades = assignmentGradeDao.getAssignmentGradesByStudentId(studentId);
        } catch (DataAccessException e) {
            logger.error("No AssignmentGrades with the given studentId were found. Please check the database");
            throw new DatabaseOperationException(GET_GRADES_BY_STUDENT_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each AssignmentGrade object in the list");
        for (AssignmentGrade assignmentGrade : assignmentGrades) {
            validate(assignmentGrade);
        }
        return assignmentGrades;
    }
    
    public List<AssignmentGrade> getAssignmentGradesByAssignmentAndStudentId(long assignmentId, long studentId) throws InvalidObjectException{
        List<AssignmentGrade> assignmentGrades = Collections.emptyList();
        try {
            logger.debug(
                    "Attempting to retrieve a list of all AssignmentGrades that have the studentId = " + studentId + "and the assignmentId =" + assignmentId);
            assignmentGrades = assignmentGradeDao.getAssignmentGradesByAssignmentAndStudentId(assignmentId, studentId);
        } catch (DataAccessException e) {
            logger.error("No AssignmentGrades . Please check the database");
            throw new DatabaseOperationException(GET_GRADES_BY_STUDENT_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each AssignmentGrade object in the list");
        for (AssignmentGrade assignmentGrade : assignmentGrades) {
            validate(assignmentGrade);
        }
        return assignmentGrades;
    }

    public Page<AssignmentGrade> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<AssignmentGrade> assignmentGrades = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the AssignmentGrades");
            assignmentGrades = assignmentGradeDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to retrieve a page of all AssignmentGrades. Check the assignment_grades table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each AssignmentGrade object in the page");
        for (AssignmentGrade assignmentGrade : assignmentGrades) {
            validate(assignmentGrade);
        }
        return assignmentGrades;
    }

    public int save(AssignmentGrade assignmentGrade) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        logger.debug("Attempting to save the following AssignmentGrade object to the database: " + assignmentGrade);
        validate(assignmentGrade);
        try {
            output = assignmentGradeDao.save(assignmentGrade);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to save the AssignmentGrade. Check the database to make sure the assignment_grades table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }

    public void update(AssignmentGrade assignmentGrade) throws InvalidObjectException, DatabaseOperationException {
        logger.debug("Attempting to update the following AssignmentGrade object in the database: " + assignmentGrade);
        validate(assignmentGrade);
        try {
            assignmentGradeDao.update(assignmentGrade);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to perform update. Check the AssignmentGrade object and assignment_grades table in the database");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + assignmentGrade.getAssignmentGradeId());
        }
    }

    public void delete(long assignmentGradeId) throws DatabaseOperationException {
        logger.debug("Attempting to delete AssignmentGrade with assignmentGradeId = " + assignmentGradeId);
        try {
            assignmentGradeDao.delete(assignmentGradeId);
        } catch (DataAccessException e) {
            logger.error(
                    "AssignmentGrade deletion failed. Check the assignment_grade table in the database and verify the assignment_grade_id");
            throw new DatabaseOperationException(ASSIGNMENT_GRADE_ID_ERROR_MESSAGE);
        }
    }
}
