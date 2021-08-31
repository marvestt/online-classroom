package dev.andrylat.app.services;

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

import dev.andrylat.app.daos.SubmissionDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Submission;
import dev.andrylat.app.utilities.Utilities;

public class SubmissionService {

    @Autowired
    private SubmissionDao submissionDao;

    private static final String SUBMISSION_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";
    private static final String GET_SUBMISSIONS_BY_ASSIGNMENT_ID_ERROR_MESSAGE = "";
    private static final String NULL_SUBMISSION = "";

    private static final Logger logger = LoggerFactory.getLogger(SubmissionService.class);

    private void validate(Submission submission) throws InvalidObjectException {
        List<String> violations = Utilities.validate(submission);
        if (submission == null) {
            logger.error("Submission object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_SUBMISSION);
        }
        logger.debug("Validating the following Submission object: " + submission);
        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error("Validation of the Submission object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(
                    violationMessages + INVALID_OBJECT_ERROR_MESSAGE + submission.getSubmissionId());
        }
    }

    public Submission get(long submissionId) throws DatabaseOperationException, InvalidObjectException {
        Submission submission = null;
        try {
            logger.debug(
                    "Attempting to retrieve Submission object from the database with a submissionId = " + submissionId);
            submission = submissionDao.get(submissionId);
        } catch (DataAccessException e) {
            logger.error(
                    "The Submission could not be found in the database. Check the records to make sure the following submissionId is present in the submissions table: "
                            + submissionId);
            throw new DatabaseOperationException(SUBMISSION_ID_ERROR_MESSAGE);
        }
        return submission;
    }

    public List<Submission> getSubmissionsByAssignmentId(long assignmentId)
            throws DatabaseOperationException, InvalidObjectException {
        List<Submission> submissions = Collections.EMPTY_LIST;
        try {
            logger.debug(
                    "Attempting to retrieve a list of all Submissions that have the assignmentId = " + assignmentId);
            submissions = submissionDao.getSubmissionsByAssignmentId(assignmentId);
        } catch (DataAccessException e) {
            logger.error(
                    "No Submissions with the assignmentId were found. Please check the submissions table in the database");
            throw new DatabaseOperationException(GET_SUBMISSIONS_BY_ASSIGNMENT_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each Submission object in the list");
        for (Submission submission : submissions) {
            validate(submission);
        }
        return submissions;
    }

    public List<Submission> getSubmissionsByStudentId(long studentId)
            throws DatabaseOperationException, InvalidObjectException {
        List<Submission> submissions = Collections.EMPTY_LIST;
        try {
            logger.debug("Attempting to retrieve a list of all Submissions that have the studentId = " + studentId);
            submissions = submissionDao.getSubmissionsByStudentId(studentId);
        } catch (DataAccessException e) {
            logger.error(
                    "No Submissions with the studentId were found. Please check the submissions table in the database");
            throw new DatabaseOperationException(GET_SUBMISSIONS_BY_ASSIGNMENT_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each Submission object in the list");
        for (Submission submission : submissions) {
            validate(submission);
        }
        return submissions;
    }

    public Page<Submission> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Submission> submissions = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the Submissions");
            submissions = submissionDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve a page of all Submissions. Check the submissions table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each Submission object in the page");
        for (Submission submission : submissions) {
            validate(submission);
        }

        return submissions;
    }

    public int save(Submission submission) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        logger.debug("Attempting to save the following Submission object to the database: " + submission);
        validate(submission);
        try {
            output = submissionDao.save(submission);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to save the Submission. Check the database to make sure the submissions table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }

    public void update(Submission submission) throws InvalidObjectException, DatabaseOperationException {
        logger.debug("Attempting to update the following Submission object in the database: " + submission);
        validate(submission);
        try {
            submissionDao.update(submission);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to perform update. Check the Submission object and submissons table in the database and make sure the submission is present");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + submission.getSubmissionId());
        }
    }

    public void delete(long submissionId) throws DatabaseOperationException {
        logger.debug("Attempting to delete Submission with submissionId = " + submissionId);
        try {
            submissionDao.delete(submissionId);
        } catch (DataAccessException e) {
            logger.error(
                    "Submission deletion failed. Check the submissions table in the database and make sure the correct submission_id is used");
            throw new DatabaseOperationException(SUBMISSION_ID_ERROR_MESSAGE);
        }
    }
}
