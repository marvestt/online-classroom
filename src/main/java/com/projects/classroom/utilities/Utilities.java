package com.projects.classroom.utilities;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.projects.classroom.model.Classroom;
import com.projects.classroom.model.Student;
import com.projects.classroom.model.Teacher;

public class Utilities {
    public static <T> List<String> validate (T objectToValidate){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);
        List<String> violationMessages = violations
                                                .stream()
                                                .map(e -> e.getMessage())
                                                .collect(Collectors.toList());
        return violationMessages;
    }
    
    public static boolean checkSessionForTeacher(HttpSession session) {
        try{
            return (Long) session.getAttribute("TEACHER_ID") != null;
        }
        catch(ClassCastException e) {
        }
        return false;
    }
    
    public static Long getTeacherIdFromSession(HttpSession session) {
        return (Long)session.getAttribute("TEACHER_ID");
    }
    
    public static Long getStudentIdFromSession(HttpSession session) {
        return (Long) session.getAttribute("STUDENT_ID");
    }
    
    public static boolean checkSessionForStudent(HttpSession session) {
        try{
            return (Long) session.getAttribute("STUDENT_ID") != null;
        }
        catch(ClassCastException e) {
        }
        return false;
    }
    
    public static boolean checkSessionForClassroom(HttpSession session) {
        try{
            return (Long) session.getAttribute("SELECTED_CLASS_ID") != null;
        }
        catch(ClassCastException e) {
        }
        return false;
    }
    
    public static Long getClassroomIdFromSession(HttpSession session) {
        return (Long) session.getAttribute("SELECTED_CLASS_ID");
    }
    
}
