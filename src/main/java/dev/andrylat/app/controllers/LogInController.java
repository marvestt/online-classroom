package dev.andrylat.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogInController {

    @GetMapping(value = "/")
    public String viewLoginPage(Model model) {
        return "index";
    }
    
    
    @GetMapping(value = "/home")
    public String viewHopePage(Model model) {
        return "home";
    }
}
