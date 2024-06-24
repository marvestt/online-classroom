package com.projects.classroom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.classroom.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    
    public Optional<User> findByUsername(String username);
}
