package com.projects.classroom.service;

import java.io.InvalidObjectException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.classroom.exception.EntityNotFoundException;
import com.projects.classroom.model.Submission;
import com.projects.classroom.repository.SubmissionRepo;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepo submissionRepo;
    
    private static final String SUBMISSION_RETRIEVAL_ERROR_MESSAGE = "Submission with ID ";
    
    public Submission get(long submissionId) {
        Optional<Submission> subOpt = submissionRepo.findById(submissionId);
        if(subOpt.isPresent()) {
            return subOpt.get();
        }
        throw new EntityNotFoundException(SUBMISSION_RETRIEVAL_ERROR_MESSAGE + submissionId);
    }

    public List<Submission> getSubmissionsByAssignmentId(long assignmentId) {
        List<Submission> submissions = submissionRepo.findByAssignmentId(assignmentId);
        return submissions;
    }

    public List<Submission> getSubmissionsByStudentId(long studentId) {
        List<Submission> submissions = submissionRepo.findByStudentId(studentId);
        return submissions;
    }
    
    public Submission getSubmissionByAssignmentIdAndStudentId(long studentId, long assignmentId) {
        Optional<Submission> subOpt = submissionRepo.findByAssignmentIdAndStudentId(assignmentId, studentId);
        if(subOpt.isPresent()) {
            return subOpt.get();
        }
        throw new EntityNotFoundException(SUBMISSION_RETRIEVAL_ERROR_MESSAGE);
    }

    public List<Submission> getAll() {
        List<Submission> submissions = submissionRepo.findAll();
        return submissions;
    }

    public Submission save(@Valid Submission submission) {
        return submissionRepo.save(submission);
    }

    public Submission update(@Valid Submission submission) {
        return submissionRepo.save(submission);
    }

    public void delete(long submissionId) {
        submissionRepo.deleteById(submissionId);
    }
}
