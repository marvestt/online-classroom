package dev.andrylat.app.controllers;

import static dev.andrylat.app.utilities.Utilities.checkSessionForClassroom;
import static dev.andrylat.app.utilities.Utilities.checkSessionForStudent;
import static dev.andrylat.app.utilities.Utilities.checkSessionForTeacher;
import static dev.andrylat.app.utilities.Utilities.getClassroomFromSession;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Classroom;
import dev.andrylat.app.models.Teacher;
import dev.andrylat.app.models.User;
import dev.andrylat.app.services.ClassroomService;
import dev.andrylat.app.services.StudentService;
import dev.andrylat.app.services.TeacherService;
import dev.andrylat.app.services.UserService;

@Controller
public class MembersController {

    @Autowired
    UserService userService;
    
    @Autowired
    StudentService studentService;
    
    @Autowired 
    TeacherService teacherService;
    
    @Autowired
    ClassroomService classroomService;
    
    private static final Logger logger = LoggerFactory.getLogger(MembersController.class);
    
    @GetMapping(value="members")
    public String viewMembersPage(Model model, HttpSession session) {
        if(!checkSessionForClassroom(session)) {
            return "redirect:/home";
        }
        Classroom classroom = getClassroomFromSession(session);
        try {
            Teacher headTeacher = teacherService.get(classroom.getMainTeacherId());
            List<User> users = userService.getUsersByClassId(classroom.getClassId());
            List<User> students  = users
                                        .stream()
                                        .filter(e -> studentService.checkStudentExists(e.getUsername()))
                                        .collect(Collectors.toList());
            List<User> teachers = users
                                    .stream()
                                    .filter(e -> teacherService.checkTeacherExists(e.getUsername()) && e.getUserId() != headTeacher.getUserId())
                                    .collect(Collectors.toList());
            model.addAttribute("listOfStudents",students);
            model.addAttribute("listOfTeachers",teachers);
            model.addAttribute("headTeacher", headTeacher);
        } catch (InvalidObjectException | DatabaseOperationException ex) {
            logger.error(ex.toString());
            model.addAttribute("errorOccured",true);
        }

        if(checkSessionForStudent(session)) {
            return "members-student";
        }
        else if(checkSessionForTeacher(session)) {
            return "members-teacher";
        }
        return "redirect:/";
    }
    
    @GetMapping(value = "remove-{userId}")
    public String removeUser(Model model,
            @PathVariable(value="userId")long userId, HttpSession session) {
        if(!checkSessionForClassroom(session)) {
            return "redirect:/home";
        }
        Classroom classroom = getClassroomFromSession(session);
        classroomService.removeFromClassroom(classroom.getClassId(), userId);;
        return "redirect:/members";
    }
}
