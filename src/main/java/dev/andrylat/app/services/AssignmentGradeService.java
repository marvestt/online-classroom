package dev.andrylat.app.services;

import java.io.InvalidObjectException; 
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.andrylat.app.daos.AssignmentGradeDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Announcement;
import dev.andrylat.app.models.AssignmentGrade;
import dev.andrylat.app.utilities.Utilities;

import javax.validation.ConstraintViolation;

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

    private void validate(AssignmentGrade assignmentGrade) throws InvalidObjectException {
        List<String> violations = Utilities.validate(assignmentGrade);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                .stream()
                .collect(Collectors.joining(NEW_LINE));
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + assignmentGrade.getAssignmentGradeId());
        }
    }

    public AssignmentGrade get(long assignmentGradeId) throws DatabaseOperationException, InvalidObjectException {
        AssignmentGrade assignmentGrade = new AssignmentGrade();
        try {
            assignmentGrade = assignmentGradeDao.get(assignmentGradeId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(ASSIGNMENT_GRADE_ID_ERROR_MESSAGE);
        }
        validate(assignmentGrade);  
        return assignmentGrade;
    }

    public List<AssignmentGrade> getAssignementGradesByAssignmentId(long assignmentId) throws DatabaseOperationException, InvalidObjectException {
        List<AssignmentGrade> assignmentGrades = Collections.EMPTY_LIST;
        try {
            assignmentGrades = assignmentGradeDao.getAssignmentGradesByAssignmentId(assignmentId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_GRADES_BY_ASSIGNMENT_ID_ERROR_MESSAGE);
        }
        for(AssignmentGrade assignmentGrade : assignmentGrades) {
            validate(assignmentGrade);
        }
        return assignmentGrades;
    }
    
    public List<AssignmentGrade> getAssignementGradesByStudentId(long studentId) throws DatabaseOperationException, InvalidObjectException {
        List<AssignmentGrade> assignmentGrades = Collections.EMPTY_LIST;
        try {
            assignmentGrades = assignmentGradeDao.getAssignmentGradesByStudentId(studentId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (GET_GRADES_BY_STUDENT_ID_ERROR_MESSAGE);
        }
        for(AssignmentGrade assignmentGrade : assignmentGrades) {
            validate(assignmentGrade);
        }
        return assignmentGrades;
    }

    public Page<AssignmentGrade> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<AssignmentGrade> assignmentGrades = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            assignmentGrades = assignmentGradeDao.getAll(page);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(AssignmentGrade assignmentGrade: assignmentGrades) {
            validate(assignmentGrade);
        }
        
        return assignmentGrades;
    }

    public int save(AssignmentGrade assignmentGrade) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(assignmentGrade);
        try {
            output = assignmentGradeDao.save(assignmentGrade);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        return output;
    }

    public void update(AssignmentGrade assignmentGrade) throws InvalidObjectException, DatabaseOperationException {
        validate(assignmentGrade);
        try {
            assignmentGradeDao.update(assignmentGrade);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + assignmentGrade.getAssignmentGradeId());
        }
    }

    public void delete(long assignmentGradeId) throws DatabaseOperationException {
        try {
            assignmentGradeDao.delete(assignmentGradeId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(ASSIGNMENT_GRADE_ID_ERROR_MESSAGE);
        }
    }
}
