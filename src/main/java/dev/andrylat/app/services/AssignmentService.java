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

import dev.andrylat.app.daos.AssignmentDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Assignment;
import dev.andrylat.app.models.AssignmentGrade;
import dev.andrylat.app.utilities.Utilities;

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
    
    private void validate(Assignment assignment) throws InvalidObjectException {
        List<String> violations = Utilities.validate(assignment);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                .stream()
                .collect(Collectors.joining(NEW_LINE));
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + assignment.getAssignmentId());
        }
    }
    
    public Assignment get(long assignmentId) throws DatabaseOperationException, InvalidObjectException{
        Assignment assignment = new Assignment();
        try {
            assignment= assignmentDao.get(assignmentId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(ASSIGNMENT_ID_ERROR_MESSAGE);
        }
        validate(assignment);  
        return assignment;
    }
    
    public List<Assignment> getAssignementsByClassId(long classId) throws DatabaseOperationException, InvalidObjectException{
        List<Assignment> assignments = Collections.EMPTY_LIST;
        try {
            assignments = assignmentDao.getAssignmentsByClassId(classId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_ASSIGNMENTS_BY_CLASS_ID_ERROR_MESSAGE);
        }
        for(Assignment assignment : assignments) {
            validate(assignment);
        }
        return assignments;
    }
    
    public Page<Assignment> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Assignment> assignments = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            assignments = assignmentDao.getAll(page);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(Assignment assignment: assignments) {
            validate(assignment);
        }
        
        return assignments;
    }
    
    public int save(Assignment assignment) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(assignment);
        try {
            output = assignmentDao.save(assignment);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }
    
    public void update(Assignment assignment) throws InvalidObjectException, DatabaseOperationException{
        validate(assignment);
        try {
            assignmentDao.update(assignment);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + assignment.getAssignmentId());
        }
    }
    
    public void delete(long assignmentId) throws DatabaseOperationException{
        try {
            assignmentDao.delete(assignmentId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(ASSIGNMENT_ID_ERROR_MESSAGE);
        }
    }
}
