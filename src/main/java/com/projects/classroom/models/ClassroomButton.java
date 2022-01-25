package com.projects.classroom.models;

public class ClassroomButton {

    private Classroom classroom;
    private String teacherName;
    
    public ClassroomButton(Classroom classroom, String teacherName) {
        this.classroom = classroom;
        this.teacherName = teacherName;
    }
    
    public Classroom getClassroom() {
        return classroom;
    }
    
    @Override
    public String toString() {
        return String.format("Class Name: %s | Professor: %s\n%n",
                classroom.getName(),teacherName,classroom.getDescription());
    }
}
