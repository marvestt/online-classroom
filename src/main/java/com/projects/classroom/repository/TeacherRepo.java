package com.projects.classroom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.classroom.model.Teacher;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long>{

    public Optional<Teacher> findById(Long userId);
    public Optional<Teacher> findByUsername(String username);
}
