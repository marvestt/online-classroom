package com.projects.classroom.controller;

import java.util.List;



import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.classroom.exception.UserAlreadyExistsException;
import com.projects.classroom.model.Student;
import com.projects.classroom.model.Teacher;
import com.projects.classroom.model.User;
import com.projects.classroom.model.UserOption;
import com.projects.classroom.model.UserType;
import com.projects.classroom.service.StudentService;
import com.projects.classroom.service.TeacherService;
import com.projects.classroom.service.UserService;
import com.projects.classroom.utilities.Utilities;

@Controller
public class SignUpController {
 
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private UserService userService;
    
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
       
        List<String> validationMessages = Utilities.validate(user);
        if(!validationMessages.isEmpty()) {
            attributes.addFlashAttribute("validationMessages",validationMessages);
            return "redirect:/signup";
        }
        
        request.getSession().invalidate();
        try {
            userService.registerUser(user);
            if(userOption.getUserType() == UserType.STUDENT) {
                student.setUserInfo(user);
                student = studentService.save(student);
                attributes.addFlashAttribute("student",student);
                request.getSession().setAttribute("STUDENT_ID", student.getUserId());
            }
            else if(userOption.getUserType() == UserType.TEACHER){
                    teacher.setUserInfo(user);
                    teacher= teacherService.save(teacher);
                    attributes.addFlashAttribute("teacher",teacher);
                    request.getSession().setAttribute("TEACHER_ID", teacher.getUserId());  
            }
        }
        catch(TransactionSystemException e) {
            attributes.addFlashAttribute("errorOccured",true);
            return "redirect:/signup";
        }
        catch(UserAlreadyExistsException e) {
            validationMessages = e.getValidationMessages();
            attributes.addFlashAttribute("validationMessages", validationMessages);
            return "redirect:/signup";
        }

            return "redirect:/home";
    }
}

