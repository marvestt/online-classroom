package com.projects.classroom.controller;

import static com.projects.classroom.utilities.Utilities.checkSessionForStudent;

import static com.projects.classroom.utilities.Utilities.checkSessionForTeacher;
import static com.projects.classroom.utilities.Utilities.getStudentIdFromSession;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.classroom.model.Assignment;
import com.projects.classroom.model.Student;
import com.projects.classroom.model.Submission;
import com.projects.classroom.service.AssignmentService;
import com.projects.classroom.service.StudentService;
import com.projects.classroom.service.SubmissionService;

@Controller
@RequestMapping
public class SubmissionsController {

    @Autowired
    AssignmentService assignmentService;
    
    @Autowired
    SubmissionService submissionService;
    
    @Autowired
    StudentService studentService;
    
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
        }catch(TransactionSystemException e) {
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
        try {
            Student student = studentService.get(getStudentIdFromSession(session)); 
            Assignment assignment = assignmentService.get(assignmentId);
            Submission submission = new Submission();
            
            submission.setAssignment(assignment);
            submission.setStudent(student);
            submission.setTitle(title);
            submission.setText(content);
            
            boolean submissionExists = submissionService.checkSubmissionExists(student.getStudentId(), assignmentId); 
            if(submissionExists) {
                Submission previouSubmission = submissionService.getSubmissionByAssignmentIdAndStudentId(student.getStudentId(),assignmentId);
                if(previouSubmission.isGraded()) {
                    model.addAttribute("graded", true);
                    model.addAttribute("assignment",assignment);
                    return "submit-assignment";
                }
                else {
                    submissionService.delete(previouSubmission.getSubmissionId());
                }
            }   
            submissionService.save(submission);
        }catch(TransactionSystemException e) {
            attributes.addFlashAttribute("errorOccured",true);
            return "redirect:/submit-"+assignmentId;
        }
        
        attributes.addFlashAttribute("submitted",true);
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
            List<Student> studentsWithSubmissions = submissions.stream()
                                                               .map(Submission::getStudent)
                                                               .collect(Collectors.toList());
            model.addAttribute("listOfStudents",studentsWithSubmissions);
            model.addAttribute("assignmentId",assignmentId);
        }catch(TransactionSystemException e) {
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
            Submission submission = submissionService.getSubmissionByAssignmentIdAndStudentId(studentId, assignmentId);
            Assignment assignment = assignmentService.get(assignmentId);
            Student student = studentService.get(studentId);
            model.addAttribute("submission",submission);
            model.addAttribute("assignment",assignment);
            model.addAttribute("student",student);
        }catch(TransactionSystemException e) {
            model.addAttribute("errorOccured",true);
            return "redirect:/view-submissions-"+assignmentId;
        }

        
        return "view-submission";
    }
}










