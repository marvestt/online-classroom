package com.projects.classroom.controller;

import static com.projects.classroom.utilities.Utilities.checkSessionForClassroom;


import static com.projects.classroom.utilities.Utilities.checkSessionForTeacher;
import static com.projects.classroom.utilities.Utilities.getClassroomIdFromSession;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.classroom.model.Announcement;
import com.projects.classroom.model.Classroom;
import com.projects.classroom.service.AnnouncementService;
import com.projects.classroom.service.ClassroomService;

@Controller
public class AnnouncementController {

    @Autowired
    AnnouncementService announcementService;
    
    @Autowired
    ClassroomService classroomService;
    
    @GetMapping(value="create-announcement")
    public String createAnnouncement(Model model,
            @ModelAttribute(value="title") String title,
            @ModelAttribute(value ="content") String content,
            RedirectAttributes attributes,
            HttpSession session) {
        if(!checkSessionForTeacher(session)) {
            return "redirect:/";
        }
        else if(!checkSessionForClassroom(session)) {
            return "redirect:/home";
        }
        Classroom classroom = classroomService.get(getClassroomIdFromSession(session));
        Announcement announcement = new Announcement();
        announcement.setClassroom(classroom);
        announcement.setTitle(title);
        announcement.setText(content);
        
        try {
            announcementService.save(announcement);
        }
        catch(TransactionSystemException e) {
            attributes.addFlashAttribute("errorOccured",true);
            return "redirect:/write-post-announcement";
        }
        
        return "redirect:/classroom";

    }
}
