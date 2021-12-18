package dev.andrylat.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dev.andrylat.app.models.Student;
import dev.andrylat.app.models.Teacher;
import dev.andrylat.app.models.User;
import dev.andrylat.app.services.StudentService;
import dev.andrylat.app.services.TeacherService;
import dev.andrylat.app.services.UserLogInService;

@Controller
public class LogInController {
    
    @Autowired
    private UserLogInService userLogInService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping(value = "/")
    public String viewLoginPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }
    
    
    @GetMapping(value = "/home")
    public String viewHopePage(Model model) {
        return "home";
    }
    
    @PostMapping(value = "/login")
    public String attemptLogin(@ModelAttribute("user") User user, Model model) {
        List<String> validationMessages = userLogInService.validateUser(user);
        
        if(!validationMessages.isEmpty()) {
            model.addAttribute("validationMessages", validationMessages);
            return "login";
        }
        
        String username = user.getUsername();
        
        if(studentService.checkStudentExists(username)) {
            Student student = studentService.getStudentByUsername(username);
            model.addAttribute("student",student);
            return "home";
        }
        else if(teacherService.checkTeacherExists(username)) {
            Teacher teacher = teacherService.getTeacherByUsername(username);
            model.addAttribute("teacher", teacher);
            return "home";
        }
        
        return "home";
    }
    

    
}
