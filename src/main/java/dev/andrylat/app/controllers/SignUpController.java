package dev.andrylat.app.controllers;

import java.io.InvalidObjectException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Student;
import dev.andrylat.app.models.Teacher;
import dev.andrylat.app.models.User;
import dev.andrylat.app.models.UserOption;
import dev.andrylat.app.models.UserType;
import dev.andrylat.app.services.StudentService;
import dev.andrylat.app.services.TeacherService;
import dev.andrylat.app.services.UserRegistrationService;
import dev.andrylat.app.services.UserService;

@Controller
public class SignUpController {

    @Autowired
    private UserService userService;
 
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired 
    private UserRegistrationService userRegistrationService;
    
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
    public RedirectView attemptSignUp(@ModelAttribute("user") User user,
            @ModelAttribute("student") Student student, @ModelAttribute("teacher") Teacher teacher,
            @ModelAttribute("userOption") UserOption userOption, RedirectAttributes attributes) {
        
        List<String> validationMessages = userRegistrationService.validateUser(user);
        
        if(!validationMessages.isEmpty()) {
            RedirectView view = new RedirectView("/signup", true);
            attributes.addFlashAttribute("validationMessages",validationMessages);
            return view;
        }
        
        userService.encryptUserPassword(user);
        
        try {
            if(userOption.getUserType() == UserType.STUDENT) {
                student.setUserInfo(user);
                studentService.save(student);
                return new RedirectView("home");
            }
            else if(userOption.getUserType() == UserType.TEACHER){
                teacher.setUserInfo(user);
                teacherService.save(teacher);
                return new RedirectView("home");
            }
        }
        catch (InvalidObjectException e) {
            e.printStackTrace();
        }
        catch(DatabaseOperationException e) {
            e.printStackTrace();
        }
        
        return new RedirectView("signup");
    }
}