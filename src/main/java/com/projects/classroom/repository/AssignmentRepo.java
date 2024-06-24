package com.projects.classroom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.classroom.model.Assignment;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, Long>{

    public List<Assignment> findByClassroomId(Long classID);
}
