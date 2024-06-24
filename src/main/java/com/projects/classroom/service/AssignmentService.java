package com.projects.classroom.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.classroom.exception.EntityNotFoundException;
import com.projects.classroom.model.Assignment;
import com.projects.classroom.repository.AssignmentRepo;


@Service
public class AssignmentService {

   @Autowired
   private AssignmentRepo assignmentRepo;
   
   private static final String ASSIGNMENT_RETRIEVAL_ERROR_MESSAGE = "Assignment with ID ";

    public Assignment get(long assignmentId) {
        Optional<Assignment> assignmentOpt = assignmentRepo.findById(assignmentId);
        if(assignmentOpt.isPresent()) {
            return assignmentOpt.get();
        }
        
        throw new EntityNotFoundException(ASSIGNMENT_RETRIEVAL_ERROR_MESSAGE + assignmentId);
    }

    public List<Assignment> getAssignementsByClassroomId(long classId) {
        List<Assignment> assignments = assignmentRepo.findByClassroomId(classId);
        
        return assignments;
    }

    public List<Assignment> getAll() {
        List <Assignment> assignments = assignmentRepo.findAll();
        
        return assignments;
    }

    public Assignment save(@Valid Assignment assignment){
        return assignmentRepo.save(assignment);
    }

    public Assignment update(@Valid Assignment assignment) {
        return assignmentRepo.save(assignment);
    }

    public void delete(long assignmentId) {
        assignmentRepo.deleteById(assignmentId);
    }
}
