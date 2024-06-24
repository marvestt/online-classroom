package com.projects.classroom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.classroom.model.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    public Optional<Student> findById(Long userId);
    public Optional<Student> findByUsername(String username);
}
