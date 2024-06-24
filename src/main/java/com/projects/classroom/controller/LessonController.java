package com.projects.classroom.controller;

import static com.projects.classroom.utilities.Utilities.checkSessionForClassroom;

import static com.projects.classroom.utilities.Utilities.checkSessionForStudent;
import static com.projects.classroom.utilities.Utilities.checkSessionForTeacher;
import static com.projects.classroom.utilities.Utilities.getClassroomIdFromSession;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.classroom.model.Classroom;
import com.projects.classroom.model.Lesson;
import com.projects.classroom.service.ClassroomService;
import com.projects.classroom.service.LessonService;

@Controller
public class LessonController {

    @Autowired
    LessonService lessonService;
    
    @Autowired
    ClassroomService classroomService;
    
    @GetMapping(value="lessons")
    public String viewLessonPage(Model model, HttpSession session) {
        if(!checkSessionForClassroom(session)) {
            return "redirect:/home";
        }
        Classroom classroom = classroomService.get(getClassroomIdFromSession(session));

        List<Lesson> lessons = lessonService.getLessonsByClassId(classroom.getClassroomId());
        model.addAttribute("listOfLessons", lessons);
        
        if(checkSessionForStudent(session)) {
            return "lessons-student";
        }
        if(checkSessionForTeacher(session)) {
            return "lessons-teacher";
        }
        return "redirect:/";
    }
    
    @GetMapping(value="create-lesson")
    public String createLesson(Model model,
            @ModelAttribute(value="title") String title,
            @ModelAttribute(value ="content") String content,
            RedirectAttributes attributes,
            HttpSession session) {
        if(!checkSessionForTeacher(session)) {
            return "redirect:/";
        }
        else if(!checkSessionForClassroom(session)) {
            return "redirect:/home";
        }
        Classroom classroom = classroomService.get(getClassroomIdFromSession(session));
        Lesson lesson = new Lesson();
        lesson.setClassroom(classroom);
        lesson.setTitle(title);
        lesson.setText(content);
        classroom.getLessons().add(lesson);
        
        lessonService.save(lesson);
        classroomService.save(classroom);
            
        return "redirect:/lessons";
    }
}
