package com.projects.classroom.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.classroom.exception.EntityNotFoundException;
import com.projects.classroom.exception.UserAlreadyExistsException;
import com.projects.classroom.model.Student;
import com.projects.classroom.repository.StudentRepo;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;
    
    @Autowired
    private UserService userService;
    
    private static final String STUDENT_RETRIEVAL_ERROR_MESSAGE = "Student with ID ";
    private static final String STUDENT_USERNAME_RETRIEVAL_ERROR_MESSAGE = "Student with username ";
    
    public Student get(long studentId){
       Optional<Student> studentOpt = studentRepo.findById(studentId);
       if(studentOpt.isPresent()) {
           return studentOpt.get();
       }
       
       throw new EntityNotFoundException(STUDENT_RETRIEVAL_ERROR_MESSAGE + studentId);
    }

    public Student getStudentByUserId(long userId){
        Optional<Student> studentOpt = studentRepo.findById(userId);
        if(studentOpt.isPresent()) {
            return studentOpt.get();
        }
        throw new EntityNotFoundException(STUDENT_RETRIEVAL_ERROR_MESSAGE + userId);
    }
    
    public Student getStudentByUsername(String username) {
        Optional<Student> studentOpt = studentRepo.findByUsername(username);
        if(studentOpt.isPresent()) {
            return studentOpt.get();
        }
        throw new EntityNotFoundException(STUDENT_USERNAME_RETRIEVAL_ERROR_MESSAGE + username);
    }
    
    public boolean checkStudentExists(String username) {
        boolean userExists = studentRepo.findByUsername(username).isPresent();
        return userExists;
    }

    public List<Student> getAll() {
        List<Student> students = studentRepo.findAll();
        return students;
    }

    public Student save(@Valid Student student) {
        boolean userExists = userService.checkUserExists(student.getUsername());
        if(!userExists) {
            userService.registerUser(student);
            return studentRepo.save(student);
        }
        throw new UserAlreadyExistsException(student.getUsername());
    }

    public void delete(long studentId){
        studentRepo.deleteById(studentId);
    }
}
