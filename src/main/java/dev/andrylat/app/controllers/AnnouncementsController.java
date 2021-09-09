package dev.andrylat.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/announcements")
public class AnnouncementsController {

    @GetMapping
    public String viewAnnouncementsPage(Model model) {
        return "announcements";
    }
}
