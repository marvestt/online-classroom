package com.projects.classroom.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.classroom.model.Student;
import com.projects.classroom.model.Teacher;
import com.projects.classroom.model.User;
import com.projects.classroom.service.StudentService;
import com.projects.classroom.service.TeacherService;

@Controller
public class LogInController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping(value = "/")
    public String viewLoginPage(Model model, HttpServletRequest request) {
        User user = new User();
        request.getSession().invalidate();
        model.addAttribute("user", user);
        return "login";
    }
    
    
    @GetMapping(value = "/home")
    public String viewHomePage(Model model, HttpSession session) {
        Long studentId = (Long)session.getAttribute("STUDENT_ID");
        Long teacherId = (Long)session.getAttribute("TEACHER_ID");
        if(studentId == null && teacherId == null) {
            return "redirect:/";
        }
        else if(studentId != null) {
            return "home-student";
        }
        else {
            return "home-teacher";
        }
    }
    
    @PostMapping(value = "/login")
    public String attemptLogin(@ModelAttribute("user") User user, Model model,
                                RedirectAttributes attributes, HttpServletRequest request) {
        
        String username = user.getUsername();
        request.getSession().invalidate();
        if(studentService.checkStudentExists(username)) {
            Student student = studentService.getStudentByUsername(username);
            attributes.addFlashAttribute("student",student);
            request.getSession().setAttribute("STUDENT_ID", student.getUserId());
            return "redirect:/home";
        }
        else if(teacherService.checkTeacherExists(username)) {
            Teacher teacher = teacherService.getTeacherByUsername(username);
            attributes.addFlashAttribute("teacher",teacher);
            request.getSession().setAttribute("TEACHER_ID", teacher.getUserId());
            return "redirect:/home";
        }
        
        attributes.addFlashAttribute("validationMessages",List.of("User credentials are incorrect. Please try again"));
        return "redirect:/";
    }
    

    
}
