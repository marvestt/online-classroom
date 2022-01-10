package dev.andrylat.app.controllers;

import static dev.andrylat.app.utilities.Utilities.checkSessionForStudent;
import static dev.andrylat.app.utilities.Utilities.checkSessionForTeacher;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/grades")
public class GradesController {

    @GetMapping
    public String viewGradesPage(Model model, HttpSession session) {
        if(checkSessionForStudent(session)) {
            return "grades-student";
        }
        else if(checkSessionForTeacher(session)) {
            return "grades-teacher";
        }
        return "redirect:/";
    }
    
    
}
