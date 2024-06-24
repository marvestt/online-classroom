package com.projects.classroom.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.classroom.exception.EntityNotFoundException;
import com.projects.classroom.exception.UserAlreadyExistsException;
import com.projects.classroom.model.Teacher;
import com.projects.classroom.repository.TeacherRepo;

@Service
public class TeacherService {
    
    @Autowired
    private TeacherRepo teacherRepo;
    
    @Autowired
    private UserService userService;
    
    private static final String TEACHER_RETRIEVAL_ERROR_MESSAGE = "Teacher with ID ";
    private static final String USER_RETRIEVAL_ERROR_MESSAGE = "User with ID ";
    private static final String USERNAME_RETRIEVAL_ERROR_MESSAGE = "Teacher with username";

    public Teacher get(long teacherId) {
        Optional<Teacher> teacherOpt = teacherRepo.findById(teacherId);
        if(teacherOpt.isPresent()) {
            return teacherOpt.get();
        }
        throw new EntityNotFoundException(TEACHER_RETRIEVAL_ERROR_MESSAGE + teacherId);
    }

    public Teacher getTeachersByUserId(long userId){
        Optional<Teacher> teacherOpt = teacherRepo.findById(userId);
        if(teacherOpt.isPresent()) {
            return teacherOpt.get();
        }
        throw new EntityNotFoundException(USER_RETRIEVAL_ERROR_MESSAGE + userId);
    }

    public List<Teacher> getAll() {
        List<Teacher> teachers = teacherRepo.findAll();
        return teachers;
    }
    
    public Teacher getTeacherByUsername(String username) {
        Optional<Teacher> teacherOpt = teacherRepo.findByUsername(username);
        if(teacherOpt.isPresent()) {
            return teacherOpt.get();
        }
        throw new EntityNotFoundException(USERNAME_RETRIEVAL_ERROR_MESSAGE + username);
    }
    
    public boolean checkTeacherExists(String username) {
        boolean userExists = teacherRepo.findByUsername(username).isPresent();
        return userExists;
    }

    public Teacher save(@Valid Teacher teacher) {
        boolean userExists = userService.checkUserExists(teacher.getUsername());
        if(!userExists) {
            userService.registerUser(teacher);
            return teacherRepo.save(teacher);
        }
        throw new UserAlreadyExistsException(teacher.getUsername());
    }


    public void delete(long teacherId) {
        teacherRepo.deleteById(teacherId);
    }
}
