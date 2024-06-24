package com.projects.classroom.controller;

import static com.projects.classroom.utilities.Utilities.checkSessionForClassroom;
import static com.projects.classroom.utilities.Utilities.checkSessionForStudent;
import static com.projects.classroom.utilities.Utilities.checkSessionForTeacher;
import static com.projects.classroom.utilities.Utilities.getClassroomIdFromSession;
import static com.projects.classroom.utilities.Utilities.getStudentIdFromSession;
import static com.projects.classroom.utilities.Utilities.getTeacherIdFromSession;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.aspectj.apache.bcel.generic.TargetLostException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.classroom.exception.DatabaseOperationException;
import com.projects.classroom.model.Announcement;
import com.projects.classroom.model.Classroom;
import com.projects.classroom.model.ClassroomButton;
import com.projects.classroom.model.Teacher;
import com.projects.classroom.model.User;
import com.projects.classroom.service.AnnouncementService;
import com.projects.classroom.service.ClassroomService;
import com.projects.classroom.service.StudentService;
import com.projects.classroom.service.TeacherService;
import com.projects.classroom.service.UserService;

@Controller
public class ClassroomController {
    
    @Autowired
    UserService userService;
    
    @Autowired
    StudentService studentService;
    
    @Autowired
    ClassroomService classroomService;
    
    @Autowired
    TeacherService teacherService;
    
    @Autowired
    AnnouncementService announcementService;
    
    @GetMapping(value = "write-post-{postType}")
    public String showCreatePostPage(Model model, HttpSession session,
            @PathVariable(value="postType") String postType) {
        model.addAttribute("postType",postType);
        return "new-post";
    }
    
    @PostMapping(value="create-post-{postType}")
    public String createPost(Model model, HttpSession session,
        @PathVariable(value="postType") String postType,
        @ModelAttribute(value="title") String title,
        @ModelAttribute(value="content") String content,
        RedirectAttributes attributes){
        if(!checkSessionForTeacher(session)) {
            return "redirect:/";
        }
        else if(!checkSessionForClassroom(session)) {
            return "redirect:/home";
        }
        attributes.addFlashAttribute("title",title);
        attributes.addFlashAttribute("content",content);
        if(postType.equalsIgnoreCase("lesson")) {
            return "redirect:/create-lesson";
        }
        else if(postType.equalsIgnoreCase("assignment")) {
            return "redirect:/create-assignment";
        }
        else if(postType.equalsIgnoreCase("announcement")) {
            return "redirect:/create-announcement";
        }
        return "redirect:/write-post-"+postType;
    }
    
    @GetMapping(value = "classroom")
    public String showClassroomPage(Model model, HttpSession session) {
        if(!checkSessionForClassroom(session)) {
            return "redirect:/home";
        }
        try {
            Classroom classroom = classroomService.get(getClassroomIdFromSession(session));

            List<Announcement> announcements = announcementService.getAnnouncementsByClassId(classroom.getClassroomId());
            model.addAttribute("listOfAnnouncements",announcements);

            model.addAttribute("classroomName", classroom.getName());
        }
        catch(TransactionSystemException e) {
            model.addAttribute("errorOccured",true);
        }
        
        if(checkSessionForStudent(session)) {
            return "classroom-home-student";
        }
        else if(checkSessionForTeacher(session)) {
            return "classroom-home-teacher";
        }
        return "redirect:/";
    }
    
    @GetMapping(value = "classroom-join-{classroomId}")
    public String classroomJoin(Model model, HttpSession session,
            @PathVariable("classroomId") int id
            ) {
        
        Classroom classroom = classroomService.get(id);
        model.addAttribute("classroomId",id);
        model.addAttribute("className", classroom.getName());

        if(checkSessionForStudent(session)) {
            return "join-classroom-student";
        }
        else if(checkSessionForTeacher(session)) {
            return "join-classroom-teacher";
        }
        return "redirect:/";
    }
    
    @GetMapping(value="send-join-request-{classroomId}")
    public String sendClassroomJoinRequest(Model model, 
            @PathVariable(value="classroomId") int classroomId, HttpSession session,
            RedirectAttributes attributes) {
        User user;
        if(checkSessionForStudent(session)) {
            user = studentService.get(getStudentIdFromSession(session));
        }
        else if(checkSessionForTeacher(session)) {
            user = teacherService.get(getTeacherIdFromSession(session));
        }
        else {
            return "redirect:/";
        }
        try {
            long userId = user.getUserId();
            Classroom classroom = classroomService.get(classroomId);
            boolean isUserRegistered = classroomService.isUserRegisteredForClass(userId, classroomId);
            
            if(!isUserRegistered) {
                classroomService.registerUserToClass(userId, classroomId);
                attributes.addFlashAttribute("joined",true);
            }

            session.setAttribute("SELECTED_CLASS_ID", classroom.getClassroomId()); 
        }
        catch(TransactionSystemException e) {
            attributes.addFlashAttribute("failedRegistration",true);
            return "redirect:/send-join-request-" + classroomId;
        }
        
        return "redirect:/classroom";
    }
    
    @GetMapping(value="select-class-{classroomId}")
    public String selectClass(Model model, HttpSession session,
            @PathVariable(value = "classroomId") int classroomId) {

        try {
            Classroom selectedClassroom = classroomService.get(classroomId);
            session.setAttribute("SELECTED_CLASS_ID", selectedClassroom.getClassroomId());
        }
        catch(TransactionSystemException e) {
            return "redirect:/my-classrooms";
        }
        
        return "redirect:/classroom";
    }
    
    @GetMapping(value = "new-classroom")
    public String showCreateClassroomPage(Model model, HttpSession session) {
        if(!checkSessionForTeacher(session)) {
            return "redirect:/";
        }
        Classroom classroom = new Classroom();
        model.addAttribute("classroom",classroom);
        return "new-classroom"; 
    }
    
    @PostMapping(value = "create-classroom")
    public String createClassroom(Model model, @ModelAttribute(value="classroom") Classroom classroom,
            HttpSession session) {
        if(!checkSessionForTeacher(session)) {
            return "redirect:/";
        }
        try {
            Teacher teacher = teacherService.get(getTeacherIdFromSession(session));
            classroom.setTeacher(teacher);
            classroom = classroomService.save(classroom);
            classroomService.registerUserToClass(teacher.getUserId(), classroom.getClassroomId());

            session.setAttribute("SELECTED_CLASS_ID", classroom.getClassroomId());
        }catch(TransactionSystemException e) {
            model.addAttribute("errorOccured",true);
            return "new-classroom";
        }
        
        return "redirect:/classroom";   
    }
    

    @GetMapping(value = "my-classrooms")
    public String showMyClassroomPage(Model model, HttpSession session) {
        User user;
        if(!checkSessionForTeacher(session) && !checkSessionForStudent(session)) {
            return "redirect:/";
        }
        else if(checkSessionForTeacher(session)) {
            user = userService.get(getTeacherIdFromSession(session));
        }
        else {
            user = userService.get(getStudentIdFromSession(session));
        }
        
        List<Classroom> classrooms = user.getClassrooms();
        
        List<ClassroomButton> classroomButtons = new ArrayList<>();
        for(Classroom classroom : classrooms) {
            classroomButtons.add(classroomToClassroomButton(classroom));
        }
        model.addAttribute("classroomButtons",classroomButtons);
        if(checkSessionForStudent(session)) {
            return "my-classrooms-student";
        }
        return "my-classrooms-teacher";
    }
    
    @GetMapping(value = "search-classrooms")
    public String searchClassrroms(Model model, @ModelAttribute(value="searchText") String searchText,
               HttpSession session) {
        if(!checkSessionForTeacher(session) && !checkSessionForStudent(session)) {
            return "redirect:/";
        }
        try {
            List<Classroom> classrooms = classroomService.searchClassrooms(searchText);
            List<ClassroomButton> classroomButtons = classrooms
                                                            .stream()
                                                            .map(e -> classroomToClassroomButton(e))
                                                            .collect(Collectors.toList());
            model.addAttribute("searchText",searchText);
            model.addAttribute("classroomButtons",classroomButtons);
            model.addAttribute("selectedClassroom",new Classroom());
        }catch(TransactionSystemException e) {
            model.addAttribute("errorOccured",true);
        }

        if(checkSessionForStudent(session)) {
            return "classroom-search-student";
        }
        return "classroom-search-teacher";
    }
    
    private ClassroomButton classroomToClassroomButton(Classroom classroom) {
        return new ClassroomButton(classroom,classroom.getTeacher().getProfessionalName());
    }
    

        

}
