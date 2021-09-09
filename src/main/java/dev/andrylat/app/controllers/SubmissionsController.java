package dev.andrylat.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/submissions")
public class SubmissionsController {

    @GetMapping
    public String viewSubmissionsPage(Model model) {
        return "submissions";
    }
}
