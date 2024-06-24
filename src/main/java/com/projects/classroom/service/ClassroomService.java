package com.projects.classroom.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.classroom.exception.DatabaseOperationException;
import com.projects.classroom.exception.EntityNotFoundException;
import com.projects.classroom.model.Classroom;
import com.projects.classroom.model.Student;
import com.projects.classroom.model.Teacher;
import com.projects.classroom.model.User;
import com.projects.classroom.repository.ClassroomRepo;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class ClassroomService {
    
    @Autowired
    private ClassroomRepo classroomRepo;
    
    @Autowired
    private UserService userService;
    
    private static final String CLASSROOM_RETRIEVAL_ERROR_MESSAGE = "Classroom with ID ";
    
    public Classroom get(long classId) {
        Optional<Classroom> classroomOpt = classroomRepo.findById(classId);
        if(classroomOpt.isPresent()) {
            return classroomOpt.get();
        }

        throw new EntityNotFoundException(CLASSROOM_RETRIEVAL_ERROR_MESSAGE + classId);
    }
    
    public List<Classroom> searchClassrooms(String searchText) throws DatabaseOperationException{
        List<Classroom> searchByNameResults = classroomRepo.findByNameContaining(searchText);
        List<Classroom> searchByDescriptionResults = classroomRepo.findByDescriptionContaining(searchText);
        List<Classroom> searchByTeacherResults = classroomRepo.findByTeacherProfessionalNameContaining(searchText);
        Set<Classroom> searchResults = new HashSet<>();
        searchResults.addAll(searchByNameResults);
        searchResults.addAll(searchByDescriptionResults);
        searchResults.addAll(searchByTeacherResults);

        return new ArrayList<>(searchResults);
    }
    
    public Classroom registerUserToClass(long userId, long classId) {
        Classroom classroom = get(classId);
        User user = userService.get(userId);
        classroom.getUsers().add(user);
        user.getClassrooms().add(classroom);
        userService.save(user);
        classroomRepo.save(classroom);
        return classroom;
    }
    
    public boolean isUserRegisteredForClass(long userId, long classId) {
        User user = userService.get(userId);
        boolean isUserRegistered = user.getClassrooms().stream()
                                                          .map(Classroom::getClassroomId)
                                                          .anyMatch(c -> c == classId);                   
        return isUserRegistered;
    }

    public List<Classroom> getAll() {
        List<Classroom> classrooms = classroomRepo.findAll();
        return classrooms;
    }

    public Classroom save(@Valid Classroom classroom) {
        return classroomRepo.save(classroom);
    }

    public Classroom update(@Valid Classroom classroom)  {
        return classroomRepo.save(classroom);
    }

    public void delete(long classId) {
        classroomRepo.deleteById(classId);
    }
    
    public Classroom removeUser(long classId, long userId) {
        Classroom classroom = get(classId);
        User user = userService.get(userId);
        classroom.getUsers().removeIf(u -> u.getUserId() == userId);
        user.getClassrooms().removeIf(c -> c.getClassroomId() == classId);
        classroomRepo.save(classroom);
        userService.save(user);
        return classroom;
    }
}
