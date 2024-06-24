package com.projects.classroom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.classroom.model.Classroom;

@Repository
public interface ClassroomRepo extends JpaRepository<Classroom, Long> {
    
    public List<Classroom> findByNameContaining(String searchText);
    public List<Classroom> findByDescriptionContaining(String searchText);
    public List<Classroom> findByTeacherProfessionalNameContaining(String searchText);
    
}
