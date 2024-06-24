package com.projects.classroom.controller;

import static com.projects.classroom.utilities.Utilities.checkSessionForClassroom;


import static com.projects.classroom.utilities.Utilities.checkSessionForStudent;
import static com.projects.classroom.utilities.Utilities.checkSessionForTeacher;
import static com.projects.classroom.utilities.Utilities.getClassroomIdFromSession;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.classroom.model.Assignment;
import com.projects.classroom.model.Classroom;
import com.projects.classroom.service.AssignmentService;
import com.projects.classroom.service.ClassroomService;
@Controller

public class AssignmentController {
    
    @Autowired
    AssignmentService assignmentService;
    
    @Autowired
    ClassroomService classroomService;
    
    @GetMapping(value = "/assignments")
    public String viewAssignmentPage(Model model, HttpSession session) {
        if(!checkSessionForClassroom(session)) {
            return "redirect:/home";
        }
        Classroom classroom = classroomService.get(getClassroomIdFromSession(session));
        List<Assignment> assignments = assignmentService.getAssignementsByClassroomId(classroom.getClassroomId());
        model.addAttribute("listOfAssignments",assignments);
        
        if(checkSessionForStudent(session)) {
            return "assignments-student";
        }
        if(checkSessionForTeacher(session)) {
            return "assignments-teacher";
        }
        return "redirect:/";
    }
    
    @GetMapping(value="create-assignment")
    public String createAssignment(Model model,
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
        Assignment assignment = new Assignment();
        assignment.setClassroom(classroom);
        assignment.setTitle(title);
        assignment.setDescription(content);
        
        assignmentService.save(assignment);
        return "redirect:/assignments";
    }
}
