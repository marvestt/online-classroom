package com.projects.classroom.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projects.classroom.exception.EntityNotFoundException;
import com.projects.classroom.model.Lesson;
import com.projects.classroom.repository.LessonRepo;

@Service
public class LessonService {

    @Autowired
    private LessonRepo lessonRepo;
    
    private static final String LESSON_RETRIEVAL_ERROR_MESSAGE = "Lesson with ID ";

    public Lesson get(long lessonId) {
        Optional<Lesson> lessonOpt = lessonRepo.findById(lessonId);
        if(lessonOpt.isPresent()) {
            return lessonOpt.get();
        }
        throw new EntityNotFoundException(LESSON_RETRIEVAL_ERROR_MESSAGE + lessonId);
    }

    public List<Lesson> getLessonsByClassId(long classId) {
        List<Lesson> lessons = lessonRepo.findByClassroomId(classId);
        return lessons;
    }

    public List<Lesson> getAll(Pageable page) {
        List<Lesson> lessons = lessonRepo.findAll();
        return lessons;
    }

    public Lesson save(@Valid Lesson lesson) {
        return lessonRepo.save(lesson);
    }

    public Lesson update(@Valid Lesson lesson){
        return lessonRepo.save(lesson);
    }

    public void delete(long lessonId) {
        lessonRepo.deleteById(lessonId);
    }
}
