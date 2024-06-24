package com.projects.classroom.service;

import java.util.List;


import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.classroom.exception.EntityNotFoundException;
import com.projects.classroom.model.Announcement;
import com.projects.classroom.repository.AnnouncementRepo;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepo announcementRepo;
    
    private static final String ANNOUNCEMENT_RETRIEVAL_ERROR_MESSAGE = "Announcement with ID ";

    public Announcement get(long announcementId) {
        Optional<Announcement> announcementOpt = announcementRepo.findById(announcementId);
        if(announcementOpt.isPresent()) {
            return announcementOpt.get();
        }
        
        throw new EntityNotFoundException(ANNOUNCEMENT_RETRIEVAL_ERROR_MESSAGE + announcementId);
    }

    public List<Announcement> getAnnouncementsByClassId(long classId) {
        List<Announcement> announcements = announcementRepo.findByClassroomId(classId);

        return announcements;
    }

    public List<Announcement> getAll(){
        List<Announcement> announcements = announcementRepo.findAll();
        return announcements;
    }

    public Announcement save(@Valid Announcement announcement) {
        return announcementRepo.save(announcement);
    }

    public Announcement update(@Valid Announcement announcement) {
        return announcementRepo.save(announcement);
    }

    public void delete(long announcementId) {
       announcementRepo.deleteById(announcementId);
    }

}
