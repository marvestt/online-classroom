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

import dev.andrylat.app.daos.SubmissionDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Assignment;
import dev.andrylat.app.models.Lesson;
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

    private static final String GET_SUBMISSIONS_BY_ASSIGNMENT_ID_ERROR_MESSAGE = null;
    
    private void validate(Submission submission) throws InvalidObjectException {
        List<String> violations = Utilities.validate(submission);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                    .stream()
                    .collect(Collectors.joining(NEW_LINE));
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + submission.getSubmissionId());
        }
    }
    
    public Submission get(long submissionId) throws DatabaseOperationException, InvalidObjectException{
        Submission submission = new Submission();
        try {
            submission= submissionDao.get(submissionId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(SUBMISSION_ID_ERROR_MESSAGE);
        }
        validate(submission);  
        return submission;
    }
    
    public List<Submission> getSubmissionsByAssignmentId(long assignmentId) throws DatabaseOperationException, InvalidObjectException{
        List<Submission> submissions = Collections.EMPTY_LIST;
        try {
            submissions = submissionDao.getSubmissionsByAssignmentId(assignmentId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_SUBMISSIONS_BY_ASSIGNMENT_ID_ERROR_MESSAGE);
        }
        for(Submission submission : submissions) {
            validate(submission);
        }
        return submissions;
    }
    
    public List<Submission> getSubmissionsByStudentId(long studentId) throws DatabaseOperationException, InvalidObjectException{
        List<Submission> submissions = Collections.EMPTY_LIST;
        try {
            submissions = submissionDao.getSubmissionsByStudentId(studentId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_SUBMISSIONS_BY_ASSIGNMENT_ID_ERROR_MESSAGE);
        }
        for(Submission submission : submissions) {
            validate(submission);
        }
        return submissions;
    }
    
    public Page<Submission> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Submission> submissions = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            submissions = submissionDao.getAll(page);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(Submission submission : submissions) {
            validate(submission);
        }
        
        return submissions;
    }
    
    public int save(Submission submission) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(submission);
        try {
            output = submissionDao.save(submission);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }
    
    public void update(Submission submission) throws InvalidObjectException, DatabaseOperationException{
        validate(submission);
        try {
            submissionDao.update(submission);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + submission.getSubmissionId());
        }
    }
    
    public void delete(long submissionId) throws DatabaseOperationException{
        try {
            submissionDao.delete(submissionId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SUBMISSION_ID_ERROR_MESSAGE);
        }
    }
}
