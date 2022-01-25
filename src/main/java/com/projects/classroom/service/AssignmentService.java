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

import com.projects.classroom.dao.AssignmentDao;
import com.projects.classroom.exception.DatabaseOperationException;
import com.projects.classroom.model.Assignment;
import com.projects.classroom.utilities.Utilities;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentDao assignmentDao;

    private static final String ASSIGNMENT_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";
    private static final String GET_ASSIGNMENTS_BY_CLASS_ID_ERROR_MESSAGE = "";
    private static final String NULL_ASSIGNMENT= "";

    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);

    private void validate(Assignment assignment) throws InvalidObjectException {
        List<String> violations = Utilities.validate(assignment);
        if (assignment == null) {
            logger.error("Assignment object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_ASSIGNMENT);
        }
        logger.debug("Validating the following Assignment object: " + assignment);
        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error("Validation of the Assignment object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(
                    violationMessages + INVALID_OBJECT_ERROR_MESSAGE + assignment.getAssignmentId());
        }
    }

    public Assignment get(long assignmentId) throws DatabaseOperationException, InvalidObjectException {
        Assignment assignment = null;
        try {
            logger.debug("Attempting to retrieve Assignment object from the database with an assignmentId = "
                    + assignmentId);
            assignment = assignmentDao.get(assignmentId);
        } catch (DataAccessException e) {
            logger.error(
                    "The Assignment could not be found in the database. Check the records to make sure the following assignmentId is present: "
                            + assignmentId);
            throw new DatabaseOperationException(ASSIGNMENT_ID_ERROR_MESSAGE);
        }
        return assignment;
    }

    public List<Assignment> getAssignementsByClassId(long classId)
            throws DatabaseOperationException, InvalidObjectException {
        List<Assignment> assignments = Collections.EMPTY_LIST;
        try {
            logger.debug("Attempting to retrieve a list of all Assignments that have the classId = " + classId);
            assignments = assignmentDao.getAssignmentsByClassId(classId);
        } catch (DataAccessException e) {
            logger.error(
                    "No Assignments with the classId were found. Please check the assignments table in the database");
            throw new DatabaseOperationException(GET_ASSIGNMENTS_BY_CLASS_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each Assignment object in the list");
        for (Assignment assignment : assignments) {
            validate(assignment);
        }
        return assignments;
    }

    public Page<Assignment> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Assignment> assignments = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the Assignments");
            assignments = assignmentDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve a page of all Assignments. Check the assignments table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each Assignment object in the page");
        for (Assignment assignment : assignments) {
            validate(assignment);
        }
        return assignments;
    }

    public int save(Assignment assignment) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        logger.debug("Attempting to save the following Assignment object to the database: " + assignment);
        validate(assignment);
        try {
            output = assignmentDao.save(assignment);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to save the Assignment. Check the database to make sure the assignments table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }

    public void update(Assignment assignment) throws InvalidObjectException, DatabaseOperationException {
        logger.debug("Attempting to update the following Assignment object in the database: " + assignment);
        validate(assignment);
        try {
            assignmentDao.update(assignment);
        } catch (DataAccessException e) {
            logger.error("Failed to perform update. Check the Assignment object and assignment table in the database");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + assignment.getAssignmentId());
        }
    }

    public void delete(long assignmentId) throws DatabaseOperationException {
        logger.debug("Attempting to delete Assignment with assignmentId = " + assignmentId);
        try {
            assignmentDao.delete(assignmentId);
        } catch (DataAccessException e) {
            logger.error(
                    "Assignment deletion failed. Check the assignment table in the database and verify the assignment_id");
            throw new DatabaseOperationException(ASSIGNMENT_ID_ERROR_MESSAGE);
        }
    }
}
