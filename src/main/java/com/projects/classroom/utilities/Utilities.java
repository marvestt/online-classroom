package com.projects.classroom.utilities;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.projects.classroom.models.Classroom;
import com.projects.classroom.models.Student;
import com.projects.classroom.models.Teacher;

public class Utilities {
    
    public static String generateDatabaseIdErrorMessage(String idName) {
        String prefix = "The ";
        String suffix = " provided doesn't match any columns in the database. Please verify the value and try again";
        return prefix + idName + suffix;
    }
    
    public static String generateDatabaseGetAllErrorMessage(String entityName) {
        String prefix = "Something went wrong when trying to retrieve all of the ";
        String suffix = ". Please check the database";
        return prefix + entityName + suffix;
    }
    
    public static String generateDatabaseSaveErrorMessage(String entityName) {
        String prefix = "Something went wrong when trying to save the ";
        String suffix = " object. Please check the database.";
        return prefix + entityName + suffix;
    }
    
    public static String generateDatabaseUpdateErrorMessage(String entityName, String idName) {
        String prefix = "Something went wrong when trying to update the ";
        String suffix = " object. Please check the database where ";
        String postSuffix = "=";
        return prefix + entityName + suffix + idName + postSuffix;
    }

    public static String generateDatabaseInvalidObjectErroeMessage(String entityName, String idName) {
        String suffix = " object has an invalid state. Check field values to make sure they are valid where ";
        String postSuffix = "=";
        return entityName + suffix + idName + postSuffix;
    }
    
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
            return (Teacher) session.getAttribute("TEACHER") != null;
        }
        catch(ClassCastException e) {
        }
        return false;
    }
    
    public static Teacher getTeacherFromSession(HttpSession session) {
        return (Teacher)session.getAttribute("TEACHER");
    }
    
    public static Student getStudentFromSession(HttpSession session) {
        return (Student) session.getAttribute("STUDENT");
    }
    
    public static boolean checkSessionForStudent(HttpSession session) {
        try{
            return (Student) session.getAttribute("STUDENT") != null;
        }
        catch(ClassCastException e) {
        }
        return false;
    }
    
    public static boolean checkSessionForClassroom(HttpSession session) {
        try{
            return (Classroom) session.getAttribute("SELECTED_CLASS") != null;
        }
        catch(ClassCastException e) {
        }
        return false;
    }
    
    public static Classroom getClassroomFromSession(HttpSession session) {
        return (Classroom) session.getAttribute("SELECTED_CLASS");
    }
    
}
