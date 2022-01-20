package dev.andrylat.app.controllers;

import static dev.andrylat.app.utilities.Utilities.checkSessionForClassroom;
import static dev.andrylat.app.utilities.Utilities.checkSessionForStudent;
import static dev.andrylat.app.utilities.Utilities.checkSessionForTeacher;
import static dev.andrylat.app.utilities.Utilities.getClassroomFromSession;
import static dev.andrylat.app.utilities.Utilities.getStudentFromSession;
import static dev.andrylat.app.utilities.Utilities.getTeacherFromSession;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Announcement;
import dev.andrylat.app.models.Classroom;
import dev.andrylat.app.models.ClassroomButton;
import dev.andrylat.app.models.Teacher;
import dev.andrylat.app.models.User;
import dev.andrylat.app.services.AnnouncementService;
import dev.andrylat.app.services.ClassroomService;
import dev.andrylat.app.services.TeacherService;

@Controller
public class ClassroomController {
    
    @Autowired
    ClassroomService classroomService;
    
    @Autowired
    TeacherService teacherService;
    
    @Autowired
    AnnouncementService announcementService;
    
    private static final Logger logger = LoggerFactory.getLogger(ClassroomController.class);
    
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
        Classroom classroom = getClassroomFromSession(session);
        String classroomName = classroom.getName();
        try {
            List<Announcement> announcements = announcementService.getAnnouncementsByClassId(classroom.getClassId());
            model.addAttribute("listOfAnnouncements",announcements);
        } catch (InvalidObjectException | DatabaseOperationException e) {
            logger.error(e.toString());
            model.addAttribute("errorOccured",true);
        }
        model.addAttribute("classroomName", classroomName);
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
        String className = "";
        try {
            className = classroomService.get(id).getName();
        } catch (InvalidObjectException | DatabaseOperationException e) {
            logger.error(e.toString());
        } 
        model.addAttribute("classroomId",id);
        model.addAttribute("className", className);

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
            user = getStudentFromSession(session);
        }
        else if(checkSessionForTeacher(session)) {
            user = getTeacherFromSession(session);
        }
        else {
            return "redirect:/";
        }
        long userId = user.getUserId();
        boolean userIsRegistered = classroomService.getClassroomsByUserId(userId)
                                                    .stream()
                                                    .map(e -> e.getClassId())
                                                    .collect(Collectors.toList())
                                                    .contains((long)classroomId);
        if(!userIsRegistered) {
            classroomService.registerUserInClassroom(userId, classroomId);
            attributes.addFlashAttribute("joined",true);
        }
        try {
            session.setAttribute("SELECTED_CLASS", classroomService.get(classroomId));
            return "redirect:/classroom";
        } catch (InvalidObjectException | DatabaseOperationException e1) {
            logger.error(e1.toString());
        }
        attributes.addFlashAttribute("failedRegistration",true);
        return "redirect:/send-join-request-" + classroomId;
    }
    
    @GetMapping(value="select-class-{classroomId}")
    public String selectClass(Model model, HttpSession session,
            @PathVariable(value = "classroomId") int classroomId) {
        try {
            Classroom selectedClassroom = classroomService.get(classroomId);
            session.setAttribute("SELECTED_CLASS", selectedClassroom);
            return "redirect:/classroom";
        } catch (InvalidObjectException | DatabaseOperationException e1) {
            logger.error(e1.toString());
        }
        return "redirect:/my-classrooms";
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
        Teacher teacher = getTeacherFromSession(session);
        classroom.setMainTeacherId(teacher.getTeacherId());
        int classId = classroomService.save(classroom);
        classroomService.registerUserInClassroom(teacher.getUserId(), classId);
        try {
            session.setAttribute("SELECTED_CLASS", classroomService.get(classId));
            return "redirect:/classroom";
        } catch (InvalidObjectException | DatabaseOperationException e1) {
            logger.error(e1.toString());
        }
        model.addAttribute("errorOccured",true);
        return "new-classroom";
    }
    

    @GetMapping(value = "my-classrooms")
    public String showMyClassroomPage(Model model, HttpSession session) {
        User user;
        if(!checkSessionForTeacher(session) && !checkSessionForStudent(session)) {
            return "redirect:/";
        }
        else if(checkSessionForTeacher(session)) {
            user = getTeacherFromSession(session);
        }
        else {
            user = getStudentFromSession(session);
        }
        List<Classroom> classrooms = classroomService.getClassroomsByUserId(user.getUserId());
        List<ClassroomButton> classroomButtons = classrooms
                                                            .stream()
                                                            .map(e -> classroomToClassroomButton(e))
                                                            .filter(e -> e!=null)
                                                            .collect(Collectors.toList());
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
        
        List<Classroom> classrooms = classroomService.searchClassrooms(searchText);
        List<ClassroomButton> classroomButtons = classrooms
                                                        .stream()
                                                        .map(e -> classroomToClassroomButton(e))
                                                        .filter(e -> e!=null)
                                                        .collect(Collectors.toList());
        model.addAttribute("searchText",searchText);
        model.addAttribute("classroomButtons",classroomButtons);
        model.addAttribute("selectedClassroom",new Classroom());
        if(checkSessionForStudent(session)) {
            return "classroom-search-student";
        }
        return "classroom-search-teacher";
    }
    
    private ClassroomButton classroomToClassroomButton(Classroom classroom) {
        try{
            return new ClassroomButton(classroom,teacherService.get(classroom.getMainTeacherId()).getProfessionalName());
        } 
        catch(InvalidObjectException ex) {return null;}
    }
    

        

}
