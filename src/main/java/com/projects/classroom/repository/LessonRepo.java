package com.projects.classroom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.classroom.model.Lesson;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Long>{

    public List<Lesson> findByClassroomId(Long classId);
}
