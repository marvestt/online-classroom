package com.projects.classroom.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
public class Teacher extends User{
    
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Classroom> classrooms;
    
    @Lob
	private String description;
	private String professionalName;
	
	public Teacher() {
	    classrooms = new ArrayList<>();
	}

	public String getProfessionalName() {
		return professionalName;
	}
	public void setProfessionalName(String professionalName) {
		this.professionalName = professionalName;
	}
	public long getTeacherId() {
		return getUserId();
	}
	public void setTeacherId(long teacherId) {
		setUserId(teacherId);;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
    public List<Classroom> getClassrooms() {
        return classrooms;
    }
    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }
    
    @Override
	public String toString() {
	    return String.format("Teacher[teacher_id=%d, description=%s, professional_name=%s]", 
                getUserId(),description,professionalName);
	}
}
