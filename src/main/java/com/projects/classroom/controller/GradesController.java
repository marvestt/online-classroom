package com.projects.classroom.controller;

import static com.projects.classroom.utilities.Utilities.checkSessionForClassroom;
import static com.projects.classroom.utilities.Utilities.checkSessionForStudent;
import static com.projects.classroom.utilities.Utilities.checkSessionForTeacher;
import static com.projects.classroom.utilities.Utilities.getClassroomFromSession;
import static com.projects.classroom.utilities.Utilities.getStudentFromSession;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.Map;
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

import com.projects.classroom.exception.DatabaseOperationException;
import com.projects.classroom.model.Assignment;
import com.projects.classroom.model.AssignmentGrade;
import com.projects.classroom.model.Classroom;
import com.projects.classroom.model.Student;
import com.projects.classroom.service.AssignmentGradeService;
import com.projects.classroom.service.AssignmentService;
import com.projects.classroom.service.StudentService;

@Controller
public class GradesController {

    @Autowired
    AssignmentGradeService assignmentGradeService;
    
    @Autowired
    AssignmentService assignmentService;
    
    @Autowired
    StudentService studentService;

    private static final Logger logger = LoggerFactory.getLogger(GradesController.class);
    
    @GetMapping("grades")
    public String viewGradesPage(Model model, HttpSession session) {
        if(checkSessionForStudent(session)) {
            Student student = getStudentFromSession(session);
            try {
                List<AssignmentGrade> grades = assignmentGradeService.getAssignementGradesByStudentId(student.getStudentId());
                Map<Long,Assignment> assignmentIdToAssignment = grades
                                                                    .stream()
                                                                    .map(e -> {try {return assignmentService.get(e.getAssignmentId());} 
                                                                        catch (InvalidObjectException | DatabaseOperationException e1) {return null;}})
                                                                    .filter(e -> e!=null)
                                                                    .collect(Collectors.toMap(e-> e.getAssignmentId(),e->e));
                model.addAttribute("listOfGrades",grades);
                model.addAttribute("assignmentIdToAssignment",assignmentIdToAssignment);
            } catch (InvalidObjectException | DatabaseOperationException e) {
                logger.error(e.toString());
                model.addAttribute("errorOccured",true);
            }
            return "grades-student";
        }
        else if(checkSessionForTeacher(session) && checkSessionForClassroom(session)) {
            Classroom classroom = getClassroomFromSession(session);
            try {
                List<Assignment> assignments = assignmentService.getAssignementsByClassId(classroom.getClassId());
                model.addAttribute("listOfAssignments",assignments);
            } catch (InvalidObjectException | DatabaseOperationException e) {
                logger.error(e.toString());
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
        
        AssignmentGrade assignmentGrade = new AssignmentGrade();
        assignmentGrade.setAssignmentId(assignmentId);
        assignmentGrade.setStudentId(studentId);
        assignmentGrade.setGrade(grade);
        
        try {
            List<AssignmentGrade> assignmentGrades = assignmentGradeService.getAssignmentGradesByAssignmentAndStudentId(assignmentId, studentId);
            for(AssignmentGrade currentGrade : assignmentGrades) {
                if(currentGrade.getAssignmentId() == assignmentId && currentGrade.getStudentId() == studentId) {
                    assignmentGradeService.delete(currentGrade.getAssignmentGradeId());
                }
            }
            assignmentGradeService.save(assignmentGrade);
            attributes.addFlashAttribute("gradeSubmitted",true);
        } catch (InvalidObjectException | DatabaseOperationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("errorOccured",true);
        }
        
        return String.format("redirect:/view-submission-%d-%d",assignmentId,studentId);
    }
    
    @GetMapping(value = "view-grades-for-assignment-{assignmentId}")
    public String viewGradesForAssignment(Model model,
            @PathVariable(value="assignmentId") long assignmentId) {
        try {
            Assignment assignment = assignmentService.get(assignmentId);
            List<AssignmentGrade> grades = assignmentGradeService.getAssignementGradesByAssignmentId(assignmentId);
            Map<Long,Student> studentIdToStudent = grades
                                                        .stream()
                                                        .map(e -> { try {return studentService.get( e.getStudentId());}
                                                                catch (InvalidObjectException | DatabaseOperationException e1) {return null;} })
                                                        .filter(e -> e!=null)
                                                        .collect(Collectors.toMap(e -> e.getStudentId(), e -> e));
            model.addAttribute("assignment",assignment);
            model.addAttribute("listOfGrades",grades);
            model.addAttribute("studentIdToStudent",studentIdToStudent);
        } catch (InvalidObjectException | DatabaseOperationException e) {
            logger.error(e.toString());
            model.addAttribute("errorOccured",true);
        }
        return "grades-by-assignment";
    }
    
    
}
