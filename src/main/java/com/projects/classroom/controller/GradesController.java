package com.projects.classroom.controller;

import static com.projects.classroom.utilities.Utilities.checkSessionForClassroom;

import static com.projects.classroom.utilities.Utilities.checkSessionForStudent;
import static com.projects.classroom.utilities.Utilities.checkSessionForTeacher;
import static com.projects.classroom.utilities.Utilities.getClassroomIdFromSession;
import static com.projects.classroom.utilities.Utilities.getStudentIdFromSession;

import java.util.List;

import javax.servlet.http.HttpSession;

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

import com.projects.classroom.model.Assignment;
import com.projects.classroom.model.Classroom;
import com.projects.classroom.model.Student;
import com.projects.classroom.model.Submission;
import com.projects.classroom.service.AssignmentService;
import com.projects.classroom.service.ClassroomService;
import com.projects.classroom.service.StudentService;
import com.projects.classroom.service.SubmissionService;

@Controller
public class GradesController {

    @Autowired
    SubmissionService submissionService;
    
    @Autowired
    AssignmentService assignmentService;
    
    @Autowired
    StudentService studentService;
    
    @Autowired
    ClassroomService classroomService;
    
    @GetMapping("grades")
    public String viewGradesPage(Model model, HttpSession session) {

        if(checkSessionForStudent(session)) {
            try {
                Student student = studentService.get(getStudentIdFromSession(session));
                List<Submission> submissions = submissionService.getSubmissionsByStudentId(student.getStudentId());
                model.addAttribute("listOfSubmissions",submissions);
            }
            catch(TransactionSystemException e) {
                model.addAttribute("errorOccured",true);
            }
            return "grades-student";
        }
        else if(checkSessionForTeacher(session) && checkSessionForClassroom(session)) {
            try {
                Classroom classroom = classroomService.get(getClassroomIdFromSession(session));
                List<Assignment> assignments = assignmentService.getAssignementsByClassroomId(classroom.getClassroomId());
                model.addAttribute("listOfAssignments",assignments);
            }catch(TransactionSystemException e) {
                model.addAttribute("errorOccured",true);
            }
            return "grades-teacher";
        }
        return "redirect:/";
    }
    
    @PostMapping(value="submit-grade-{assignmentId}-{studentId}")
    public String submitGrade(Model model,HttpSession session,
            @PathVariable(value="studentId") long studentId,
            @PathVariable(value="assignmentId") long assignmentId,
            @ModelAttribute(value="grade") String grade,
            RedirectAttributes attributes) {
        try {
            Submission submission = submissionService.getSubmissionByAssignmentIdAndStudentId(studentId, assignmentId);
            submission.setGrade(grade);
            submission.setGraded(true);
            
            submissionService.save(submission);
            attributes.addFlashAttribute("gradeSubmitted",true);
        }catch(TransactionSystemException e) {
            model.addAttribute("errorOccured", true);
        }
        return String.format("redirect:/view-submission-%d-%d",assignmentId,studentId);
    }
    
    @GetMapping(value = "view-grades-for-assignment-{assignmentId}")
    public String viewGradesForAssignment(Model model,
            @PathVariable(value="assignmentId") long assignmentId) {

        try {
            Assignment assignment = assignmentService.get(assignmentId);
            List<Submission> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId);
            
            model.addAttribute("assignment",assignment);
            model.addAttribute("listOfSubmissions",submissions);
        }catch(TransactionSystemException e) {
            model.addAttribute("errorOccured",true);
        }
        
        return "grades-by-assignment";
    }
    
    
}
