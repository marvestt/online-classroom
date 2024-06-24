package com.projects.classroom.controller;

import java.util.List;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.projects.classroom.model.UserOption;
import com.projects.classroom.model.UserType;
import com.projects.classroom.service.StudentService;
import com.projects.classroom.service.TeacherService;
import com.projects.classroom.service.UserService;

@Controller
public class SignUpController {
 
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping("/signup")
    public String attemptSignup(Model model) {
        User user = new User();
        Student student = new Student();
        Teacher teacher = new Teacher();;
        UserOption userOption = new UserOption();
        userOption.setUserType(UserType.STUDENT);
        model.addAttribute("user",user);
        model.addAttribute("teacher",teacher);
        model.addAttribute("student",student);
        model.addAttribute("userOption",userOption);
        return "signup";
    }
    
    @PostMapping(value = "/initiate-signup")
    public String attemptSignUp(@ModelAttribute("user") User user,
            @ModelAttribute("student") Student student, @ModelAttribute("teacher") Teacher teacher,
            @ModelAttribute("userOption") UserOption userOption, RedirectAttributes attributes, HttpServletRequest request) {
       
        request.getSession().invalidate();
        
        if(userOption.getUserType() == UserType.STUDENT) {
            student.setUserInfo(user);
            studentService.save(student);
            student = studentService.getStudentByUsername(student.getUsername());
            attributes.addFlashAttribute("student",student);
            request.getSession().setAttribute("STUDENT_ID", student.getUserId());
            return "redirect:/home";
        }
        else if(userOption.getUserType() == UserType.TEACHER){
            teacher.setUserInfo(user);
            teacherService.save(teacher);
            teacher= teacherService.getTeacherByUsername(teacher.getUsername());
            attributes.addFlashAttribute("teacher",teacher);
            request.getSession().setAttribute("TEACHER_ID", teacher.getUserId());
            return "redirect:/home";
        }
    
        
        
        return "redirect:/signup";
    }
}