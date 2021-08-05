package dev.andrylat.model;

import java.util.List;

public class Teacher extends User{

	private long teacherId;
	private String teacherDescription;
	private List<Classroom> classesCreated;
	
	
	public List<Classroom> getClassesCreated() {
		return classesCreated;
	}
	public void setClassesCreated(List<Classroom> classesCreated) {
		this.classesCreated = classesCreated;
	}
	public long getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherDescription() {
		return teacherDescription;
	}
	public void setTeacherDescription(String teacherDescription) {
		this.teacherDescription = teacherDescription;
	}
	
	
}
