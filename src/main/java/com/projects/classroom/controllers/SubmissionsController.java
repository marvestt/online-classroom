package com.projects.classroom.controllers;

import static com.projects.classroom.utilities.Utilities.checkSessionForStudent;
import static com.projects.classroom.utilities.Utilities.checkSessionForTeacher;
import static com.projects.classroom.utilities.Utilities.getStudentFromSession;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.classroom.exceptions.DatabaseOperationException;
import com.projects.classroom.models.Assignment;
import com.projects.classroom.models.Student;
import com.projects.classroom.models.Submission;
import com.projects.classroom.services.AssignmentService;
import com.projects.classroom.services.StudentService;
import com.projects.classroom.services.SubmissionService;

@Controller
@RequestMapping
public class SubmissionsController {

    @Autowired
    AssignmentService assignmentService;
    
    @Autowired
    SubmissionService submissionService;
    
    @Autowired
    StudentService studentService;
    
    private static final Logger logger = LoggerFactory.getLogger(SubmissionsController.class);
    
    @GetMapping(value="submit-{assignmentId}")
    public String viewSubmissionsPage(Model model,
            @PathVariable(value="assignmentId") long assignmentId,
            HttpSession session) {
        if(!checkSessionForStudent(session)) {
            return "redirect:/";
        }
        try {
            Assignment assignment = assignmentService.get(assignmentId);
            model.addAttribute("assignment",assignment);
        } catch (InvalidObjectException | DatabaseOperationException e) {
            logger.error(e.toString());
            model.addAttribute("errorOccured",true);
        }
        return "submit-assignment";
    }
    
    @PostMapping(value="submit-assignment-{assignmentId}")
    public String submitAssignment(Model model,
            @PathVariable(value="assignmentId") long assignmentId,
            @ModelAttribute(value="title") String title,
            @ModelAttribute(value="content") String content,
            HttpSession session,
            RedirectAttributes attributes) {
        if(!checkSessionForStudent(session)) {
            return "redirect:/";
        }
        Student student = getStudentFromSession(session); 
        Submission submission = new Submission();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(student.getStudentId());
        submission.setTitle(title);
        submission.setText(content);
        try {
            List<Submission> submissionsForAssignment = submissionService.getSubmissionsByAssignmentId(assignmentId);
            List<Submission> previousSubmissions = submissionsForAssignment
                                                                .stream()
                                                                .filter(e-> e.getStudentId() == student.getStudentId())
                                                                .collect(Collectors.toList());
            
            previousSubmissions.forEach(e-> submissionService.delete(e.getSubmissionId()));
            submissionService.save(submission);
            attributes.addFlashAttribute("submitted",true);
        } catch (InvalidObjectException | DatabaseOperationException e) {
            logger.error(e.toString());
            attributes.addFlashAttribute("errorOccured",true);
        }
        
        return "redirect:/submit-"+assignmentId;
    }
    
    @GetMapping(value ="view-submissions-{assignmentId}")
    public String viewStudentSubmissions(Model model,
            @PathVariable(value="assignmentId") long assignmentId,
            HttpSession session) {
        if(!checkSessionForTeacher(session)) {
            return "redirect:/";
        }
        try {
            List<Submission> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId);
            List<Student> studentsWithSubmissions = submissions
                                                            .stream()
                                                            .map(e -> {try {return studentService.get(e.getStudentId());} 
                                                                        catch (InvalidObjectException| DatabaseOperationException e1) {return null;}})
                                                            .filter(e-> e!=null)
                                                            .collect(Collectors.toList());
            model.addAttribute("listOfStudents",studentsWithSubmissions);
            model.addAttribute("assignmentId",assignmentId);
        } catch (InvalidObjectException | DatabaseOperationException e) {
            logger.error(e.toString());
            model.addAttribute("errorOccured",true);
        }
        return "view-submissions";
    }
    
    @GetMapping(value="view-submission-{assignmentId}-{studentId}")
    public String viewSubmission(Model model,HttpSession session,
            @PathVariable(value="assignmentId") long assignmentId,
            @PathVariable(value="studentId") long studentId) {
        if(!checkSessionForTeacher(session)) {
            return"redirect:/";
        }
        try {
            Submission submission = submissionService.getSubmissionByStudentAndAssignmentId(studentId, assignmentId);
            Assignment assignment = assignmentService.get(assignmentId);
            Student student = studentService.get(studentId);
            model.addAttribute("submission",submission);
            model.addAttribute("assignment",assignment);
            model.addAttribute("student",student);
            return "view-submission";
        } catch (DatabaseOperationException | InvalidObjectException e) {
            logger.error(e.toString());
            model.addAttribute("errorOccured",true);
        }
        return "redirect:/view-submissions-"+assignmentId;
    }
}










