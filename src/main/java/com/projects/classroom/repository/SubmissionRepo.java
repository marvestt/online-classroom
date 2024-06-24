package com.projects.classroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.classroom.model.Submission;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, Long>{
    
    public List<Submission> findByAssignmentId(Long assignmentId);
    public List<Submission> findByStudentId(Long studentId);
    public Optional<Submission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
}
