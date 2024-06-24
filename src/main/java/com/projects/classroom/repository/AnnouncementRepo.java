package com.projects.classroom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.classroom.model.Announcement;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long>{
    
    List<Announcement> findByClassroomId(Long classId);

}
